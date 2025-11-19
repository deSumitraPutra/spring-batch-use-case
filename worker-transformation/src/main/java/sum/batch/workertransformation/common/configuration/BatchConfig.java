package sum.batch.workertransformation.common.configuration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    /**
     * Setting H2 as Spring Batch Metadata storage is not recommended because the data is in-memory
     * which can be disappeared when service restart
     */
    @Bean
    public JobRepository jobRepository(
        @Qualifier("dataSource") DataSource dataSource,
        @Qualifier("transactionManager") PlatformTransactionManager transactionManager
    ) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setDatabaseType(DatabaseType.H2.toString());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    /**
     * JobLauncher set to use ThreadPoolTaskExecutor because multiple kafka message that arrive at almost
     * same time can trigger the multiple job using the same Threads.
     * By default, Spring Batch JobLauncher executed at synchronously.
     */
    @Bean
    public JobLauncher jobLauncher(@Qualifier("jobRepository") JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();

        return jobLauncher;
    }

    /**
     * JobLauncher set to use ThreadPoolTaskExecutor because multiple kafka message that arrive at almost
     * same time can trigger the multiple job using the same Threads.
     * By default, Spring Batch JobLauncher executed at synchronously.
     */
    @Bean
    public JobLauncher asyncJobLauncher(@Qualifier("jobRepository") JobRepository jobRepository) throws Exception {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // minimum always alive thread
        executor.setMaxPoolSize(8); // max thread for bursts
        executor.setQueueCapacity(16); // how many pending jobs before blocking
        executor.setWaitForTasksToCompleteOnShutdown(true); // graceful stop on shutdown
        executor.setAwaitTerminationSeconds(60); // wait before force kill
        executor.setThreadNamePrefix("batch-job-"); // for log clarity
        executor.initialize();

        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(executor);
        jobLauncher.afterPropertiesSet();

        return jobLauncher;
    }

    /**
     * This initializer is not recommended for production-grade service. Because, this part is executed
     * everytime the service is started. That can cause error if not configured correctly. It may cause
     * duplicate table creation, etc.
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSource") DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-h2.sql"));
        populator.setContinueOnError(false);
        populator.setIgnoreFailedDrops(false);

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}