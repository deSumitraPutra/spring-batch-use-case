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
import sum.batch.workertransformation.common.constant.WorkerDB.ProductTransactionQuery;
import sum.batch.workertransformation.common.listener.BatchListener;
import sum.batch.workertransformation.model.ProductTransaction;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ProductTransactionStep {
    private final DataSource mainDataSource;
    private final DataSource workerDataSource;
    private final BatchListener batchListener;

    @Bean
    public Step productTransactionStagingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("productTransactionStagingStep", jobRepository)
            .<ProductTransaction, ProductTransaction>chunk(1000, transactionManager)
            .reader(productTransactionStagingReader())
            .writer(productTransactionStagingWriter())
            .listener(this.batchListener)
            .build();
    }

    @Bean
    public JdbcPagingItemReader<ProductTransaction> productTransactionStagingReader() {
        JdbcPagingItemReader<ProductTransaction> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(this.mainDataSource);
        reader.setFetchSize(1000);
        reader.setPageSize(1000);
        reader.setRowMapper(((rs, rowNum) -> ProductTransaction.fromResultSet(rs)));
        reader.setQueryProvider(productTransactionQueryProvider());

        return reader;
    }

    private SqlServerPagingQueryProvider productTransactionQueryProvider() {
        SqlServerPagingQueryProvider provider = new SqlServerPagingQueryProvider();
        provider.setSelectClause(ProductTransactionQuery.SELECT_CLAUSE);
        provider.setFromClause(ProductTransactionQuery.FROM_CLAUSE);
        provider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));

        return provider;
    }

    @Bean
    public JdbcBatchItemWriter<ProductTransaction> productTransactionStagingWriter() {
        JdbcBatchItemWriter<ProductTransaction> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(this.workerDataSource);
        writer.setSql(ProductTransactionQuery.INSERT_ROW);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        return writer;
    }
}
