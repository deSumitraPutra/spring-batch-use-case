package sum.batch.workerstagingdata.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonQuery {
    public static Map<String, String> totalRowQuery() {
        return Map.of(
            JobName.PRODUCT, OracleDB.ProductQuery.TOTAL_ROWS,
            JobName.TRANSACTION, OracleDB.TransactionQuery.TOTAL_ROWS,
            JobName.PRODUCT_TRANSACTION, OracleDB.ProductTransactionQuery.TOTAL_ROWS
        );
    }
}
