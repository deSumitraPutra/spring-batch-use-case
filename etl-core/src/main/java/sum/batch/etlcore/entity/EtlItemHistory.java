package sum.batch.etlcore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sum.batch.etlcore.enums.JobStatus;
import sum.batch.etlcore.enums.JobType;

import java.sql.Date;
import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtlItemHistory {
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
}
