package in.bushansirgur.expensetrackerapi.dto;

import in.bushansirgur.expensetrackerapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    private String categoryId;

    private String name;

    private String description;

    private String categoryIcon;

    private User user;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
