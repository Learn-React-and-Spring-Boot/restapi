package in.bushansirgur.expensetrackerapi.service;

import in.bushansirgur.expensetrackerapi.dto.CategoryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAll(Pageable page);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    void deleteCategory(String categoryId);
}
