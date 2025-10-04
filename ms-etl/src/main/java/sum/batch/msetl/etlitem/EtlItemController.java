package sum.batch.msetl.etlitem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sum.batch.etlcore.dto.EtlRequestDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/etls")
public class EtlItemController {
    private final EtlItemService service;

    @PostMapping
    public ResponseEntity<Void> initiateETL(@RequestBody List<EtlRequestDTO> requests) {
        this.service.initiate(requests);

        return ResponseEntity.noContent().build();
    }
}
