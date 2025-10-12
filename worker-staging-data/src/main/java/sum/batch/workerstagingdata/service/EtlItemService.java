package sum.batch.workerstagingdata.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static sum.batch.workerstagingdata.common.constant.MainDB.EtlItemQuery.*;

@Repository
@RequiredArgsConstructor
public class EtlItemService {
    private final JdbcTemplate mainJdbcTemplate;

    @Transactional(transactionManager = "mainTransactionManager")
    public void updateToRunning(String jobName) {
        int affectedRow = this.mainJdbcTemplate.update(MARK_AS_RUNNING, jobName);
        this.validateResult(jobName, affectedRow);
    }

    @Transactional(transactionManager = "mainTransactionManager")
    public void finalized(String status, String message, String jobName) {
        int affectedRow = this.mainJdbcTemplate.update(MARK_FINAL, status, message, jobName);
        this.validateResult(jobName, affectedRow);
    }

    private void validateResult(String jobName, int affectedRow) {
        if (affectedRow > 1) {
            String errorMessage = "Etl Item with job name: " + jobName + " has multiple row in database!";
            throw new IllegalArgumentException(errorMessage);
        }
        if (affectedRow == 0) {
            String errorMessage = "Etl Item with job name: " + jobName + " not found!";
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void createHistory(String jobName) {
        this.mainJdbcTemplate.execute(
            INSERT_HISTORY,
            (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, jobName);
                return ps.execute();
            }
        );
    }
}
