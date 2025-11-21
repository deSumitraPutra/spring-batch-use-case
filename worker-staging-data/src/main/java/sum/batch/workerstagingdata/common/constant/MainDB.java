package sum.batch.workerstagingdata.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MainDB {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class EtlItemQuery {
        public static final String MARK_AS_RUNNING =
            "UPDATE etl_item " +
            "SET " +
                "status = 'RUNNING', " +
                "start_time = SYSUTCDATETIME(), " +
                "updated_at = SYSUTCDATETIME() " +
            "WHERE job_name = ? AND status = 'INITIATED'";

        public static final String MARK_FINAL =
            "UPDATE etl_item " +
            "SET " +
                "status = ?, " +
                "end_time = SYSUTCDATETIME(), " +
                "duration = DATEDIFF(MILLISECOND, start_time, SYSUTCDATETIME()), " +
                "updated_at = SYSUTCDATETIME(), " +
                "message = ? " +
            "WHERE job_name = ? AND status = 'RUNNING'";

        public static final String INSERT_HISTORY =
            "INSERT INTO etl_item_history (" +
                "business_date, " +
                "job_name, " +
                "job_type, " +
                "status, " +
                "start_time, " +
                "end_time, " +
                "duration, " +
                "message, " +
                "created_at, " +
                "updated_at) " +
            "SELECT " +
                "business_date, " +
                "job_name, " +
                "job_type, " +
                "status, " +
                "start_time, " +
                "end_time, " +
                "duration, " +
                "message, " +
                "created_at, " +
                "updated_at " +
            "FROM etl_item " +
            "WHERE job_name = ?";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class ProductQuery {
        public static final String INSERT_ROW =
            "INSERT INTO stg_product (" +
                "code, " +
                "name, " +
                "description, " +
                "price, " +
                "created_at) " +
            "VALUES (" +
                ":code, " +
                ":name, " +
                ":description, " +
                ":price, " +
                ":createdAt)";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class TransactionQuery {
        public static final String INSERT_ROW =
            "INSERT INTO stg_transaction (" +
                "identifier, " +
                "status, " +
                "created_at, " +
                "updated_at) " +
            "VALUES (" +
                ":identifier, " +
                ":status, " +
                ":createdAt, " +
                ":updatedAt)";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class ProductTransactionQuery {
        public static final String INSERT_ROW =
            "INSERT INTO stg_product_transaction (" +
                "transaction_id, " +
                "product_id, " +
                "product_count) " +
            "VALUES (" +
                ":transactionId, " +
                ":productId, " +
                ":productCount)";
    }

}
