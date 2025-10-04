package sum.batch.etlcore.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sum.batch.etlcore.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sum.batch.etlcore.enums.JobType;

import java.sql.Date;
import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtlItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date businessDate;

    @Column(nullable = false, unique = true)
    private String jobName;

    @Enumerated(value = EnumType.STRING)
    private JobType jobType;

    @Enumerated(value = EnumType.STRING)
    private JobStatus status;

    private Instant startTime;

    private Instant endTime;

    private Long duration;

    private String message;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public EtlItemHistory convertToHistory() {
        return EtlItemHistory.builder()
            .businessDate(this.businessDate)
            .jobName(this.jobName)
            .jobType(this.jobType)
            .status(this.status)
            .startTime(this.startTime)
            .endTime(this.endTime)
            .duration(this.duration)
            .message(this.message)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }
}
