package sum.batch.workerstagingdata.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sum.batch.workerstagingdata.batch.step.CommonStep;
import sum.batch.workerstagingdata.common.constant.JobName;
import sum.batch.workerstagingdata.common.constant.MainDB;
import sum.batch.workerstagingdata.common.constant.OracleDB;
import sum.batch.workerstagingdata.common.listener.BatchListener;
import sum.batch.workerstagingdata.common.listener.JobCompletionListener;
import sum.batch.workerstagingdata.model.Transaction;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class TransactionJob {
    private final DataSource oracleDataSource;
    private final DataSource mainDataSource;
    private final JobCompletionListener jobCompletionListener;
    private final BatchListener batchListener;
    private final CommonStep commonStep;

    @Bean
    public Job transactionExtractJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JobName.TRANSACTION, jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(commonStep.markAsRunningStep(jobRepository, transactionManager, JobName.TRANSACTION))
            .next(transactionMigrateStep(jobRepository, transactionManager))
            .listener(jobCompletionListener)
            .build();
    }

    @Bean
    public Step transactionMigrateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("transactionMigrateStep", jobRepository)
            .<Transaction, Transaction>chunk(1000, transactionManager)
            .reader(transactionReader(null))
            .writer(transactionWriter())
            .listener(batchListener)
            .faultTolerant()
            .build();
    }

    @Bean
    @StepScope
    public ItemReader<Transaction> transactionReader(@Value("#{jobParameters['businessDate']}") String transactionDate) {
        JdbcPagingItemReader<Transaction> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(oracleDataSource);
        reader.setQueryProvider(selectTransactionQuery(transactionDate));
        reader.setRowMapper((rs, rowNum) -> Transaction.fromResultSet(rs));
        reader.setPageSize(1000);
        reader.setFetchSize(1000);

        return reader;
    }

    private PagingQueryProvider selectTransactionQuery(String transactionDate) {
        OraclePagingQueryProvider factory = new OraclePagingQueryProvider();
        factory.setSelectClause(OracleDB.TransactionQuery.SELECT_CLAUSE);
        factory.setFromClause(OracleDB.TransactionQuery.FROM_CLAUSE);
        factory.setWhereClause(String.format(OracleDB.TransactionQuery.WHERE_CLAUSE, transactionDate));
        factory.setSortKeys(Collections.singletonMap("rid", org.springframework.batch.item.database.Order.ASCENDING));

        return factory;
    }

    @Bean
    public ItemWriter<Transaction> transactionWriter() {
        return new JdbcBatchItemWriterBuilder<Transaction>()
            .dataSource(mainDataSource)
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql(MainDB.TransactionQuery.INSERT_ROW)
            .build();
    }
}
