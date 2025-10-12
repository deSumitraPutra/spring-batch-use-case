package sum.batch.workerstagingdata.batch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sum.batch.workerstagingdata.service.EtlItemService;

@Configuration
@RequiredArgsConstructor
public class CommonStep {
    private final EtlItemService etlItemService;

    public Step markAsRunningStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, String jobName) {
        String stepName = "MARK_AS_RUNNING -> " + jobName;
        return new StepBuilder(stepName, jobRepository)
            .tasklet((contribution, chunkContext) -> {
                etlItemService.updateToRunning(jobName);
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
    }
}
