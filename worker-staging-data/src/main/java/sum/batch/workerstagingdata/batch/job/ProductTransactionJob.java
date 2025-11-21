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
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sum.batch.workerstagingdata.batch.step.CommonStep;
import sum.batch.workerstagingdata.common.constant.JobName;
import sum.batch.workerstagingdata.common.constant.MainDB;
import sum.batch.workerstagingdata.common.constant.OracleDB;
import sum.batch.workerstagingdata.common.listener.BatchListener;
import sum.batch.workerstagingdata.common.listener.JobCompletionListener;
import sum.batch.workerstagingdata.model.ProductTransaction;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ProductTransactionJob {
    private final DataSource oracleDataSource;
    private final DataSource mainDataSource;
    private final JobCompletionListener jobCompletionListener;
    private final BatchListener batchListener;
    private final CommonStep commonStep;

    @Bean
    public Job productTransactionStagingJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JobName.PRODUCT_TRANSACTION, jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(commonStep.markAsRunningStep(jobRepository, transactionManager, JobName.PRODUCT_TRANSACTION))
            .next(migrateProductTransactionStep(jobRepository, transactionManager))
            .listener(jobCompletionListener)
            .build();
    }

    @Bean
    public Step migrateProductTransactionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("migrateProductTransactionStep", jobRepository)
            .<ProductTransaction, ProductTransaction>chunk(1000, transactionManager)
            .reader(productTransactionReader())
            .writer(productTransactionWriter())
            .listener(batchListener)
            .faultTolerant()
            .build();
    }

    @Bean
    @StepScope
    public ItemReader<ProductTransaction> productTransactionReader() {
        JdbcPagingItemReader<ProductTransaction> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(oracleDataSource);
        reader.setQueryProvider(selectProductQuery());
        reader.setRowMapper((rs, rowNum) -> ProductTransaction.fromResultSet(rs));
        reader.setPageSize(1000);
        reader.setFetchSize(1000);

        return reader;
    }

    private PagingQueryProvider selectProductQuery() {
        OraclePagingQueryProvider factory = new OraclePagingQueryProvider();
        factory.setSelectClause(OracleDB.ProductTransactionQuery.SELECT_CLAUSE);
        factory.setFromClause(OracleDB.ProductTransactionQuery.FROM_CLAUSE);
        factory.setSortKeys(Collections.singletonMap("rid", Order.ASCENDING));

        return factory;
    }

    @Bean
    public ItemWriter<ProductTransaction> productTransactionWriter() {
        return new JdbcBatchItemWriterBuilder<ProductTransaction>()
            .dataSource(mainDataSource)
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql(MainDB.ProductTransactionQuery.INSERT_ROW)
            .build();
    }
}
