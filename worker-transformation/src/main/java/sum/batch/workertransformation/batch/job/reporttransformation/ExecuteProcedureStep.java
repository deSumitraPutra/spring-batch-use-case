package sum.batch.workertransformation.batch.job.reporttransformation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ExecuteProcedureStep {
    private final DataSource workerDataSource;

    @Bean
    public Step transformReport(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("transformReport", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                try {
                    log.info("Executing transform report stored procedure");
                    Connection connection = this.workerDataSource.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call [dbo].[SP_TRANSFORM_REPORT]}");
                    callableStatement.execute();
                    log.info("Successfully executed procedure");

                    return RepeatStatus.FINISHED;
                } catch (SQLException exception) {
                    log.error("Something went wrong while executing procedure", exception);
                    throw exception;
                }
            }, transactionManager)
            .build();
    }
}
