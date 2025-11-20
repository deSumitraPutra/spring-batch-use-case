package sum.batch.workertransformation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    private String identifier;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static Transaction fromResultSet(ResultSet rs) throws SQLException {
        return Transaction.builder()
            .identifier(rs.getString("IDENTIFIER"))
            .status(rs.getString("STATUS"))
            .createdAt(rs.getDate("CREATED_AT"))
            .updatedAt(rs.getDate("UPDATED_DATE"))
            .build();
    }
}
