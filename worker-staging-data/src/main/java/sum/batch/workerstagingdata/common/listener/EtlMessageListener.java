package sum.batch.workerstagingdata.common.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sum.batch.workerstagingdata.model.ExtractionPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class EtlMessageListener {
    private final JobLauncher asyncJobLauncher;
    private final JobRegistry jobRegistry;

    @KafkaListener(topics = "${kafka.staging.topic}")
    public void listen(String message) {
        log.info("Successfully receive message: {}", message);

        try {
            ExtractionPayload payload = new ObjectMapper().readValue(message, ExtractionPayload.class);
            JobParameters jobParameters = payload.getJobParameters();
            Job job = jobRegistry.getJob(payload.getJobName());

            asyncJobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
