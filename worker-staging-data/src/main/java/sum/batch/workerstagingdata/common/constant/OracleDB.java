package sum.batch.workerstagingdata.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OracleDB {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class ProductQuery {
        public static final String SELECT_CLAUSE = "SELECT ROWID as rid, p.*";
        public static final String FROM_CLAUSE = "FROM PRODUCT p";
        public static final String TOTAL_ROWS = String.format("SELECT COUNT(*) %s", FROM_CLAUSE);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class TransactionQuery {
        public static final String SELECT_CLAUSE = "SELECT ROWID as rid, tx.*";
        public static final String FROM_CLAUSE = "FROM TRANSACTION tx";
        public static final String WHERE_CLAUSE = "WHERE tx.CREATED_AT <= TO_DATE('%s', 'YYYY-MM-DD')";
        public static final String TOTAL_ROWS = String.format("SELECT COUNT(*) %s %s", FROM_CLAUSE, WHERE_CLAUSE);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class ProductTransactionQuery {
        public static final String SELECT_CLAUSE = "SELECT ROWID as rid, pt.*";
        public static final String FROM_CLAUSE = "FROM PRODUCT_TRANSACTION pt";
        public static final String TOTAL_ROWS = String.format("SELECT COUNT(*) %s", FROM_CLAUSE);
    }

}
