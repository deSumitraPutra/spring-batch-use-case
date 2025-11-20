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
import sum.batch.workertransformation.common.constant.WorkerDB.ProductQuery;
import sum.batch.workertransformation.common.listener.BatchListener;
import sum.batch.workertransformation.model.Product;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ProductStep {
    private final DataSource mainDataSource;
    private final DataSource workerDataSource;
    private final BatchListener batchListener;

    @Bean
    public Step productStagingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("productStagingStep", jobRepository)
            .<Product, Product>chunk(1000, transactionManager)
            .reader(productStagingReader())
            .writer(productStagingWriter())
            .listener(this.batchListener)
            .build();
    }

    @Bean
    public JdbcPagingItemReader<Product> productStagingReader() {
        JdbcPagingItemReader<Product> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(this.mainDataSource);
        reader.setFetchSize(1000);
        reader.setPageSize(1000);
        reader.setRowMapper(((rs, rowNum) -> Product.fromResultSet(rs)));
        reader.setQueryProvider(productQueryProvider());

        return reader;
    }

    private SqlServerPagingQueryProvider productQueryProvider() {
        SqlServerPagingQueryProvider provider = new SqlServerPagingQueryProvider();
        provider.setSelectClause(ProductQuery.SELECT_CLAUSE);
        provider.setFromClause(ProductQuery.FROM_CLAUSE);
        provider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));

        return provider;
    }

    @Bean
    public JdbcBatchItemWriter<Product> productStagingWriter() {
        JdbcBatchItemWriter<Product> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(this.workerDataSource);
        writer.setSql(ProductQuery.INSERT_ROW);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        return writer;
    }
}
