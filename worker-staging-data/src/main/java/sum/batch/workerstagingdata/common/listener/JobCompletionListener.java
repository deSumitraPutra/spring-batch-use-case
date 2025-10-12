package sum.batch.workerstagingdata.common.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sum.batch.workerstagingdata.common.constant.CommonQuery;
import sum.batch.workerstagingdata.common.constant.Parameter;
import sum.batch.workerstagingdata.service.EtlItemService;
import sum.batch.workerstagingdata.service.NotificationService;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCompletionListener implements JobExecutionListener {
    private final EtlItemService etlItemService;
    private final NotificationService notificationService;
    private final JdbcTemplate oracleJdbcTemplate;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String businessDate = jobParameters.getString(Parameter.BUSINESS_DATE);
        String jobName = jobParameters.getString(Parameter.JOB_NAME);
        String totalRowsQuery = CommonQuery.totalRowQuery().get(jobName);
        String fullQuery = String.format(totalRowsQuery, businessDate);

        Integer totalRows = oracleJdbcTemplate.queryForObject(fullQuery, Integer.class);

        jobExecution.getExecutionContext().putInt(Parameter.TOTAL_ROWS, totalRows != null ? totalRows : 0);
        log.info("📊 Total rows to process: {}", totalRows);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        String newStatus = "SUCCESS";
        String errorMessage = "";

        boolean unsuccessful = jobExecution.getStatus().isUnsuccessful();
        if (unsuccessful) {
            newStatus = "FAILED";
            errorMessage = jobExecution.getAllFailureExceptions()
                .stream()
                .map(Throwable::toString)
                .collect(Collectors.joining("; "));
        }

        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        Long readCount = stepExecutions.stream()
            .mapToLong(StepExecution::getReadCount)
            .sum();
        Long writeCount = stepExecutions.stream()
            .mapToLong(StepExecution::getWriteCount)
            .sum();
        String finalMessage = String.format(
            "Job: %s is finished with status: %s. Total read row: %d. Total staged row: %d. Message: %s",
            jobName, newStatus, readCount, writeCount, errorMessage);
        log.info(finalMessage);

        this.markFinal(newStatus, errorMessage, jobName);
        this.recordHistory(jobName);
        this.sendNotification(jobName);
    }

    private void markFinal(String newStatus, String message, String jobName) {
        try {
            this.etlItemService.finalized(newStatus, message, jobName);
        } catch (Exception exception) {
            log.error("Error while marking final etl item. Cause: {}", exception.getMessage());
        }
    }

    private void recordHistory(String jobName) {
        try {
            this.etlItemService.createHistory(jobName);
        } catch (Exception exception) {
            log.error("Error while recording etl item history. Cause: {}", exception.getMessage());
        }
    }

    private void sendNotification(String message) {
        try {
            this.notificationService.sendNotification(message);
        } catch (Exception e) {
            log.error("Error while sending notification. Cause: {}", e.getMessage());
        }
    }

}
