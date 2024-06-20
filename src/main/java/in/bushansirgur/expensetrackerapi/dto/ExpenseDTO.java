package in.bushansirgur.expensetrackerapi.dto;

import in.bushansirgur.expensetrackerapi.entity.Category;
import in.bushansirgur.expensetrackerapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDTO {

    private String expenseId;
    private String name;
    private String description;
    private BigDecimal amount;
    private Date date;
    private Category category;
    private User user;
    private String categoryId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
