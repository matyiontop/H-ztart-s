package hu.haztartas.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_balance")
public class AccountBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "bank_amount", precision = 14, scale = 2)
    private BigDecimal bankAmount = BigDecimal.ZERO;

    @Column(name = "cash_amount", precision = 14, scale = 2)
    private BigDecimal cashAmount = BigDecimal.ZERO;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 255)
    private String note;

    public AccountBalance() {
        this.updatedAt = LocalDateTime.now();
    }

    public AccountBalance(BigDecimal balance, BigDecimal bankAmount, BigDecimal cashAmount, String note) {
        this.balance = balance != null ? balance : BigDecimal.ZERO;
        this.bankAmount = bankAmount != null ? bankAmount : BigDecimal.ZERO;
        this.cashAmount = cashAmount != null ? cashAmount : BigDecimal.ZERO;
        this.note = note;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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
}
