package sum.batch.workertransformation.common.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    // MAIN DATASOURCE
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.main")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager mainTransactionManager(@Qualifier("mainDataSource") DataSource mainDataSource) {
        return new DataSourceTransactionManager(mainDataSource);
    }

    // WORKER DATASOURCE
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.worker")
    public DataSource workerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager workerTransactionManager(@Qualifier("workerDataSource") DataSource workerDataSource) {
        return new DataSourceTransactionManager(workerDataSource);
    }

    // BATCH DATASOURCE
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
