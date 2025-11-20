package sum.batch.workertransformation.batch.job.reporttransformation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sum.batch.workertransformation.batch.step.CommonStep;
import sum.batch.workertransformation.common.constant.JobName;
import sum.batch.workertransformation.common.listener.JobCompletionListener;

@Configuration
@RequiredArgsConstructor
public class ReportTransformationJob {
    private final JobCompletionListener jobCompletionListener;
    private final CommonStep commonStep;

    @Bean
    public Job reportTransformationJob(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        @Qualifier("productStagingStep") Step productStagingStep,
        @Qualifier("transactionStagingStep") Step transactionStagingStep,
        @Qualifier("productTransactionStagingStep") Step productTransactionStagingStep,
        @Qualifier("transformReport") Step transformReport
    ) {
        return new JobBuilder(JobName.REPORT_TRANSFORMATION, jobRepository)
            .start(commonStep.markAsRunningStep(jobRepository, transactionManager, JobName.REPORT_TRANSFORMATION))
            .next(productStagingStep)
            .next(transactionStagingStep)
            .next(productTransactionStagingStep)
            .next(transformReport)
            .listener(jobCompletionListener)
            .build();
    }

}
