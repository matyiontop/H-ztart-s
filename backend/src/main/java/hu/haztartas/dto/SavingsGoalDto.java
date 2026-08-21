package hu.haztartas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsGoalDto {
    private Long id;

    @NotBlank(message = "A cél megnevezése nem lehet üres")
    private String name;

    @NotNull(message = "A célösszeg megadása kötelező")
    @DecimalMin(value = "1.0", message = "A célösszegnek legalább 1 Ft-nak kell lennie")
    private BigDecimal targetAmount;

    private BigDecimal currentAmount = BigDecimal.ZERO;
    private LocalDate targetDate;
    private String color = "#10b981";
    private String icon = "target";
    private String notes;

    // Számított mezők
    private double progressPercentage;
    private BigDecimal remainingAmount;
    private Long remainingMonths;
    private BigDecimal requiredMonthlySavings;

    public SavingsGoalDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Long getRemainingMonths() {
        return remainingMonths;
    }

    public void setRemainingMonths(Long remainingMonths) {
        this.remainingMonths = remainingMonths;
    }

    public BigDecimal getRequiredMonthlySavings() {
        return requiredMonthlySavings;
    }

    public void setRequiredMonthlySavings(BigDecimal requiredMonthlySavings) {
        this.requiredMonthlySavings = requiredMonthlySavings;
    }
}
