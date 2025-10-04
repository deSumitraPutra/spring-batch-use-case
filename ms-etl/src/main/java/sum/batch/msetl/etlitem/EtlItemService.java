package sum.batch.msetl.etlitem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sum.batch.etlcore.dto.EtlRequestDTO;
import sum.batch.msetl.kafka.KafkaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EtlItemService {
    private final EtlItemRepository repository;
    private final KafkaService kafkaService;

    public void initiate(List<EtlRequestDTO> requests) {
        requests.stream()
            .forEach(extractionRequestDTO -> {
                this.repository.initiate(extractionRequestDTO);
                this.kafkaService.sendMessage(extractionRequestDTO);
            });
    }
}
