package hu.haztartas.service;

import hu.haztartas.dto.ExpenseDto;
import hu.haztartas.entity.Category;
import hu.haztartas.entity.Expense;
import hu.haztartas.repository.CategoryRepository;
import hu.haztartas.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ExpenseDto> getAllExpenses() {
        return expenseRepository.findAllByOrderByExpenseDateDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> getExpensesForPeriod(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByExpenseDateBetweenOrderByExpenseDateDesc(startDate, endDate).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ExpenseDto getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kiadás nem található az azonosítóval: " + id));
        return toDto(expense);
    }

    public ExpenseDto createExpense(ExpenseDto dto) {
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }

        boolean isFixed = dto.isFixed();
        if (category != null && !dto.isFixed()) {
            isFixed = category.isFixed();
        }

        Expense expense = new Expense(
                dto.getTitle(),
                dto.getAmount(),
                isFixed,
                category,
                dto.getExpenseDate() != null ? dto.getExpenseDate() : LocalDate.now(),
                dto.getDueDayOfMonth() != null ? dto.getDueDayOfMonth() : 10,
                dto.getPriority(),
                dto.getDescription(),
                dto.isRecurring()
        );

        Expense saved = expenseRepository.save(expense);
        return toDto(saved);
    }

    public ExpenseDto updateExpense(Long id, ExpenseDto dto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kiadás nem található az azonosítóval: " + id));

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }

        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setFixed(dto.isFixed());
        expense.setCategory(category);
        if (dto.getExpenseDate() != null) {
            expense.setExpenseDate(dto.getExpenseDate());
        }
        if (dto.getDueDayOfMonth() != null) {
            expense.setDueDayOfMonth(dto.getDueDayOfMonth());
        }
        if (dto.getPriority() != null) {
            expense.setPriority(dto.getPriority());
        }
        expense.setDescription(dto.getDescription());
        expense.setRecurring(dto.isRecurring());

        Expense updated = expenseRepository.save(expense);
        return toDto(updated);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public ExpenseDto toDto(Expense entity) {
        if (entity == null) return null;
        ExpenseDto dto = new ExpenseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAmount(entity.getAmount());
        dto.setFixed(entity.isFixed());
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
            dto.setCategoryColor(entity.getCategory().getColor());
            dto.setCategoryIcon(entity.getCategory().getIcon());
        }
        dto.setExpenseDate(entity.getExpenseDate());
        dto.setDueDayOfMonth(entity.getDueDayOfMonth());
        dto.setPriority(entity.getPriority());
        dto.setDescription(entity.getDescription());
        dto.setRecurring(entity.isRecurring());
        return dto;
    }
}
