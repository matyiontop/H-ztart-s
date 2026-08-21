package hu.haztartas.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_history")
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "actual_balance", nullable = false, precision = 14, scale = 2)
    private BigDecimal actualBalance = BigDecimal.ZERO;

    @Column(name = "calculated_balance", nullable = false, precision = 14, scale = 2)
    private BigDecimal calculatedBalance = BigDecimal.ZERO;

    @Column(length = 255)
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public BalanceHistory() {
        this.createdAt = LocalDateTime.now();
    }

    public BalanceHistory(LocalDate recordDate, BigDecimal actualBalance, BigDecimal calculatedBalance, String note) {
        this.recordDate = recordDate != null ? recordDate : LocalDate.now();
        this.actualBalance = actualBalance != null ? actualBalance : BigDecimal.ZERO;
        this.calculatedBalance = calculatedBalance != null ? calculatedBalance : BigDecimal.ZERO;
        this.note = note;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.recordDate == null) {
            this.recordDate = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
