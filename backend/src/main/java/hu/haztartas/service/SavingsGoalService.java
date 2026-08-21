package hu.haztartas.service;

import hu.haztartas.dto.SavingsGoalDto;
import hu.haztartas.entity.SavingsGoal;
import hu.haztartas.repository.SavingsGoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    public List<SavingsGoalDto> getAllGoals() {
        return savingsGoalRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SavingsGoalDto getGoalById(Long id) {
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Megtakarítási cél nem található: " + id));
        return toDto(goal);
    }

    public SavingsGoalDto createGoal(SavingsGoalDto dto) {
        SavingsGoal goal = new SavingsGoal(
                dto.getName(),
                dto.getTargetAmount(),
                dto.getCurrentAmount(),
                dto.getTargetDate(),
                dto.getColor(),
                dto.getIcon(),
                dto.getNotes()
        );
        SavingsGoal saved = savingsGoalRepository.save(goal);
        return toDto(saved);
    }

    public SavingsGoalDto updateGoal(Long id, SavingsGoalDto dto) {
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Megtakarítási cél nem található: " + id));

        goal.setName(dto.getName());
        goal.setTargetAmount(dto.getTargetAmount());
        goal.setCurrentAmount(dto.getCurrentAmount());
        goal.setTargetDate(dto.getTargetDate());
        goal.setColor(dto.getColor());
        goal.setIcon(dto.getIcon());
        goal.setNotes(dto.getNotes());

        SavingsGoal updated = savingsGoalRepository.save(goal);
        return toDto(updated);
    }

    public SavingsGoalDto addDeposit(Long id, BigDecimal amount) {
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Megtakarítási cél nem található: " + id));

        BigDecimal current = goal.getCurrentAmount() != null ? goal.getCurrentAmount() : BigDecimal.ZERO;
        goal.setCurrentAmount(current.add(amount));

        SavingsGoal updated = savingsGoalRepository.save(goal);
        return toDto(updated);
    }

    public void deleteGoal(Long id) {
        savingsGoalRepository.deleteById(id);
    }

    public SavingsGoalDto toDto(SavingsGoal entity) {
        if (entity == null) return null;
        SavingsGoalDto dto = new SavingsGoalDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setTargetAmount(entity.getTargetAmount());
        dto.setCurrentAmount(entity.getCurrentAmount() != null ? entity.getCurrentAmount() : BigDecimal.ZERO);
        dto.setTargetDate(entity.getTargetDate());
        dto.setColor(entity.getColor());
        dto.setIcon(entity.getIcon());
        dto.setNotes(entity.getNotes());

        // Számítások
        BigDecimal target = entity.getTargetAmount();
        BigDecimal current = dto.getCurrentAmount();

        if (target != null && target.compareTo(BigDecimal.ZERO) > 0) {
            double percent = current.divide(target, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
            dto.setProgressPercentage(Math.min(100.0, Math.round(percent * 10.0) / 10.0));
            BigDecimal remaining = target.subtract(current);
            dto.setRemainingAmount(remaining.compareTo(BigDecimal.ZERO) > 0 ? remaining : BigDecimal.ZERO);

            if (entity.getTargetDate() != null) {
                long months = ChronoUnit.MONTHS.between(LocalDate.now(), entity.getTargetDate());
                if (months < 1) months = 1;
                dto.setRemainingMonths(months);
                if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                    dto.setRequiredMonthlySavings(remaining.divide(BigDecimal.valueOf(months), 0, RoundingMode.CEILING));
                } else {
                    dto.setRequiredMonthlySavings(BigDecimal.ZERO);
                }
            }
        }

        return dto;
    }
}
