package sum.batch.workertransformation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTransaction {
    private Long id;
    private Long transactionId;
    private Long productId;
    private Integer productCount;

    public static ProductTransaction fromResultSet(ResultSet rs) throws SQLException {
        return ProductTransaction.builder()
            .transactionId(rs.getLong("TRANSACTION_ID"))
            .productId(rs.getLong("PRODUCT_ID"))
            .productCount(rs.getInt("PRODUCT_COUNT"))
            .build();
    }
}
