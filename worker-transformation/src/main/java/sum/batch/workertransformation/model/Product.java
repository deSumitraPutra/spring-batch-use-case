package sum.batch.workertransformation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Date createdAt;

    public static Product fromResultSet(ResultSet rs) throws SQLException {
        return Product.builder()
            .code(rs.getString("CODE"))
            .name(rs.getString("NAME"))
            .description(rs.getString("DESCRIPTION"))
            .price(rs.getBigDecimal("PRICE"))
            .createdAt(rs.getDate("CREATED_AT"))
            .build();
    }
}
