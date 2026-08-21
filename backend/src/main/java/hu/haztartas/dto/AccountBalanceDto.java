package hu.haztartas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountBalanceDto {
    private Long id;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal bankAmount = BigDecimal.ZERO;
    private BigDecimal cashAmount = BigDecimal.ZERO;
    private LocalDateTime updatedAt;
    private String note;
    private BigDecimal projectedEndOfMonthBalance = BigDecimal.ZERO;

    public AccountBalanceDto() {
    }

    public AccountBalanceDto(Long id, BigDecimal balance, BigDecimal bankAmount, BigDecimal cashAmount, LocalDateTime updatedAt, String note, BigDecimal projectedEndOfMonthBalance) {
        this.id = id;
        this.balance = balance;
        this.bankAmount = bankAmount;
        this.cashAmount = cashAmount;
        this.updatedAt = updatedAt;
        this.note = note;
        this.projectedEndOfMonthBalance = projectedEndOfMonthBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(BigDecimal bankAmount) {
        this.bankAmount = bankAmount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getProjectedEndOfMonthBalance() {
        return projectedEndOfMonthBalance;
    }

    public void setProjectedEndOfMonthBalance(BigDecimal projectedEndOfMonthBalance) {
        this.projectedEndOfMonthBalance = projectedEndOfMonthBalance;
    }
}
