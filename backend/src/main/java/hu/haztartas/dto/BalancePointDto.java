package hu.haztartas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BalancePointDto {
    private String label;
    private LocalDate date;
    private BigDecimal actualBalance;
    private BigDecimal calculatedBalance;
    private BigDecimal difference;

    public BalancePointDto() {}

    public BalancePointDto(String label, LocalDate date, BigDecimal actualBalance, BigDecimal calculatedBalance) {
        this.label = label;
        this.date = date;
        this.actualBalance = actualBalance;
        this.calculatedBalance = calculatedBalance != null ? calculatedBalance : BigDecimal.ZERO;
        this.difference = actualBalance != null ? actualBalance.subtract(this.calculatedBalance) : null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getActualBalance() {
        return actualBalance;
    }

    public void setActualBalance(BigDecimal actualBalance) {
        this.actualBalance = actualBalance;
    }

    public BigDecimal getCalculatedBalance() {
        return calculatedBalance;
    }

    public void setCalculatedBalance(BigDecimal calculatedBalance) {
        this.calculatedBalance = calculatedBalance;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }
}
