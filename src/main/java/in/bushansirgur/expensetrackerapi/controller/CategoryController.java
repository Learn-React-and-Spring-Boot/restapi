package in.bushansirgur.expensetrackerapi.controller;

import in.bushansirgur.expensetrackerapi.dto.CategoryDTO;
import in.bushansirgur.expensetrackerapi.io.CategoryRequest;
import in.bushansirgur.expensetrackerapi.io.CategoryResponse;
import in.bushansirgur.expensetrackerapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryResponse> fetchUserCategories(Pageable page) {
        List<CategoryDTO> categoryDTOList = categoryService.getAll(page);
        return categoryDTOList.stream().map(categoryDTO -> mapToResponse(categoryDTO)).collect(Collectors.toList());
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createUserCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO = mapToDTO(categoryRequest);
        categoryDTO = categoryService.createCategory(categoryDTO);
        return mapToResponse(categoryDTO);
    }

    @DeleteMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    private CategoryDTO mapToDTO(CategoryRequest categoryRequest) {
        CategoryDTO returnValue = new CategoryDTO();
        BeanUtils.copyProperties(categoryRequest, returnValue);
        return returnValue;
    }

    private CategoryResponse mapToResponse(CategoryDTO categoryDTO) {
        CategoryResponse returnValue = new CategoryResponse();
        BeanUtils.copyProperties(categoryDTO, returnValue);
        return returnValue;
    }
}
