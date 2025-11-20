package sum.batch.workertransformation.batch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.SqlServerPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sum.batch.workertransformation.common.constant.WorkerDB.TransactionQuery;
import sum.batch.workertransformation.common.listener.BatchListener;
import sum.batch.workertransformation.model.Transaction;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class TransactionStep {
    private final DataSource mainDataSource;
    private final DataSource workerDataSource;
    private final BatchListener batchListener;

    @Bean
    public Step transactionStagingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("transactionStagingStep", jobRepository)
            .<Transaction, Transaction>chunk(1000, transactionManager)
            .reader(transactionStagingReader())
            .writer(transactionStagingWriter())
            .listener(this.batchListener)
            .build();
    }

    @Bean
    public JdbcPagingItemReader<Transaction> transactionStagingReader() {
        JdbcPagingItemReader<Transaction> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(this.mainDataSource);
        reader.setFetchSize(1000);
        reader.setPageSize(1000);
        reader.setRowMapper(((rs, rowNum) -> Transaction.fromResultSet(rs)));
        reader.setQueryProvider(transactionQueryProvider());

        return reader;
    }

    private SqlServerPagingQueryProvider transactionQueryProvider() {
        SqlServerPagingQueryProvider provider = new SqlServerPagingQueryProvider();
        provider.setSelectClause(TransactionQuery.SELECT_CLAUSE);
        provider.setFromClause(TransactionQuery.FROM_CLAUSE);
        provider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));

        return provider;
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> transactionStagingWriter() {
        JdbcBatchItemWriter<Transaction> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(this.workerDataSource);
        writer.setSql(TransactionQuery.INSERT_ROW);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        return writer;
    }
}
