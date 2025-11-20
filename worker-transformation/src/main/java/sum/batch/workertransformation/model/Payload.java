package sum.batch.workertransformation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import sum.batch.workertransformation.common.constant.Parameter;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payload {
    private Date businessDate;
    private String jobName;

    public JobParameters getJobParameters() {
        return new JobParametersBuilder()
            .addLong(Parameter.RUN_ID, System.currentTimeMillis())
            .addString(Parameter.BUSINESS_DATE, this.businessDate.toString())
            .addString(Parameter.JOB_NAME, this.jobName)
            .toJobParameters();
    }
}
