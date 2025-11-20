package sum.batch.workertransformation.common.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import sum.batch.workertransformation.service.EtlItemService;
import sum.batch.workertransformation.service.NotificationService;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCompletionListener implements JobExecutionListener {
    private final EtlItemService etlItemService;
    private final NotificationService notificationService;

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

        String finalMessage = String.format("Job: %s is finished with status: %s. Message: %s", jobName, newStatus, errorMessage);
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
