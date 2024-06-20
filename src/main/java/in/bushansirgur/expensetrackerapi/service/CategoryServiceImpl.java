package in.bushansirgur.expensetrackerapi.service;

import in.bushansirgur.expensetrackerapi.dto.CategoryDTO;
import in.bushansirgur.expensetrackerapi.entity.Category;
import in.bushansirgur.expensetrackerapi.exceptions.ResourceNotFoundException;
import in.bushansirgur.expensetrackerapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Override
    public List<CategoryDTO> getAll(Pageable page) {
        List<Category> categoryEntityList = categoryRepository.findByUserId(userService.getLoggedInUser().getId(), page).toList();
        return categoryEntityList.stream().map(category -> mapToDTO(category)).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(UUID.randomUUID().toString());
        categoryDTO.setUser(userService.getLoggedInUser());
        Category newCategory = mapToEntity(categoryDTO);
        newCategory = categoryRepository.save(newCategory);
        return mapToDTO(newCategory);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), categoryId);
        if (!categoryOptional.isPresent()) {
            throw new ResourceNotFoundException("Category not found for the categoryId "+categoryId);
        }
        categoryRepository.delete(categoryOptional.get());
    }

    private CategoryDTO mapToDTO(Category newCategory) {
        return CategoryDTO.builder()
                .name(newCategory.getName())
                .categoryId(newCategory.getCategoryId())
                .description(newCategory.getDescription())
                .categoryIcon(newCategory.getCategoryIcon())
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .user(newCategory.getUser())
                .build();
    }

    private Category mapToEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .categoryId(categoryDTO.getCategoryId())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .categoryIcon(categoryDTO.getCategoryIcon())
                .user(categoryDTO.getUser())
                .build();
    }


}
