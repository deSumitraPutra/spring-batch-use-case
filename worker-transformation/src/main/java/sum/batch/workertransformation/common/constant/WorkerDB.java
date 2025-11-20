package sum.batch.workertransformation.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkerDB {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class ProductQuery {
        public static final String SELECT_CLAUSE = "SELECT *";
        public static final String FROM_CLAUSE = "FROM stg_product";
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
        public static final String SELECT_CLAUSE = "SELECT *";
        public static final String FROM_CLAUSE = "FROM stg_transaction";
        public static final String INSERT_ROW =
            "INSERT INTO stg_transaction (" +
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
    public final class ProductTransactionQuery {
        public static final String SELECT_CLAUSE = "SELECT *";
        public static final String FROM_CLAUSE = "FROM stg_product_transaction";
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
