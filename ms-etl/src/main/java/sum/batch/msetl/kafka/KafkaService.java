package sum.batch.msetl.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sum.batch.etlcore.dto.EtlRequestDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.staging.topic}")
    private String stagingTopic;

    public void sendMessage(EtlRequestDTO requestDTO) {
        try {
            String message = this.objectMapper.writeValueAsString(requestDTO);
            log.info("Sending message to kafka: {}", message);
            this.kafkaTemplate.send(stagingTopic, message);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
