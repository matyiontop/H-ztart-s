package hu.haztartas.service;

import hu.haztartas.dto.CategoryDto;
import hu.haztartas.entity.Category;
import hu.haztartas.entity.enums.CategoryType;
import hu.haztartas.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getCategoriesByType(CategoryType type) {
        return categoryRepository.findByTypeOrderByNameAsc(type).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategória nem található az azonosítóval: " + id));
        return toDto(category);
    }

    public CategoryDto createCategory(CategoryDto dto) {
        Category category = new Category(
                dto.getName(),
                dto.getType(),
                dto.isFixed(),
                dto.getIcon(),
                dto.getColor(),
                dto.getMonthlyBudgetLimit(),
                dto.getPriority()
        );
        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategória nem található az azonosítóval: " + id));

        category.setName(dto.getName());
        category.setType(dto.getType());
        category.setFixed(dto.isFixed());
        category.setIcon(dto.getIcon());
        category.setColor(dto.getColor());
        category.setMonthlyBudgetLimit(dto.getMonthlyBudgetLimit());
        category.setPriority(dto.getPriority());

        Category updated = categoryRepository.save(category);
        return toDto(updated);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDto toDto(Category entity) {
        if (entity == null) return null;
        return new CategoryDto(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.isFixed(),
                entity.getIcon(),
                entity.getColor(),
                entity.getMonthlyBudgetLimit(),
                entity.getPriority()
        );
    }
}
