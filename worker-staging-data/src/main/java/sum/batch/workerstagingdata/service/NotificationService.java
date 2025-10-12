package sum.batch.workerstagingdata.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.notification.topic}")
    private String topic;

    public void sendNotification(String message) {
        this.kafkaTemplate.send(topic, message);
    }
}
