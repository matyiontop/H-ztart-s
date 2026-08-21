package hu.haztartas.service;

import hu.haztartas.dto.IncomeDto;
import hu.haztartas.entity.Category;
import hu.haztartas.entity.Income;
import hu.haztartas.repository.CategoryRepository;
import hu.haztartas.repository.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;

    public IncomeService(IncomeRepository incomeRepository, CategoryRepository categoryRepository) {
        this.incomeRepository = incomeRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<IncomeDto> getAllIncomes() {
        return incomeRepository.findAllByOrderByReceivedDateDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<IncomeDto> getIncomesForPeriod(LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findByReceivedDateBetweenOrderByReceivedDateDesc(startDate, endDate).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public IncomeDto getIncomeById(Long id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bevétel nem található az azonosítóval: " + id));
        return toDto(income);
    }

    public IncomeDto createIncome(IncomeDto dto) {
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }

        Income income = new Income(
                dto.getTitle(),
                dto.getAmount(),
                dto.getFrequency(),
                category,
                dto.getReceivedDate() != null ? dto.getReceivedDate() : LocalDate.now(),
                dto.getDescription(),
                dto.isRecurring(),
                dto.getDurationMonths()
        );

        Income saved = incomeRepository.save(income);
        return toDto(saved);
    }

    public IncomeDto updateIncome(Long id, IncomeDto dto) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bevétel nem található az azonosítóval: " + id));

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }

        income.setTitle(dto.getTitle());
        income.setAmount(dto.getAmount());
        income.setFrequency(dto.getFrequency());
        income.setCategory(category);
        if (dto.getReceivedDate() != null) {
            income.setReceivedDate(dto.getReceivedDate());
        }
        income.setDescription(dto.getDescription());
        income.setRecurring(dto.isRecurring());
        income.setDurationMonths(dto.getDurationMonths());

        Income updated = incomeRepository.save(income);
        return toDto(updated);
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    public IncomeDto toDto(Income entity) {
        if (entity == null) return null;
        IncomeDto dto = new IncomeDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAmount(entity.getAmount());
        dto.setFrequency(entity.getFrequency());
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
            dto.setCategoryColor(entity.getCategory().getColor());
            dto.setCategoryIcon(entity.getCategory().getIcon());
        }
        dto.setReceivedDate(entity.getReceivedDate());
        dto.setDescription(entity.getDescription());
        dto.setRecurring(entity.isRecurring());
        dto.setDurationMonths(entity.getDurationMonths());
        return dto;
    }
}
