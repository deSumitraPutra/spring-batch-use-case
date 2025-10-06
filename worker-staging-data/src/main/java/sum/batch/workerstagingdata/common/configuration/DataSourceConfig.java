package sum.batch.workerstagingdata.common.configuration;

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

    // ORACLE DATASOURCE
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager oracleTransactionManager(@Qualifier("oracleDataSource") DataSource oracleDataSource) {
        return new DataSourceTransactionManager(oracleDataSource);
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
