package hu.haztartas.entity;

import hu.haztartas.entity.enums.Priority;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "is_fixed", nullable = false)
    private boolean isFixed = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "due_day_of_month")
    private Integer dueDayOfMonth = 10;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Priority priority = Priority.NEEDS;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring = true;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Expense() {}

    public Expense(String title, BigDecimal amount, boolean isFixed, Category category, LocalDate expenseDate, Integer dueDayOfMonth, Priority priority, String description, boolean isRecurring) {
        this.title = title;
        this.amount = amount;
        this.isFixed = isFixed;
        this.category = category;
        this.expenseDate = expenseDate != null ? expenseDate : LocalDate.now();
        this.dueDayOfMonth = dueDayOfMonth != null ? dueDayOfMonth : 10;
        this.priority = priority != null ? priority : (category != null ? category.getPriority() : Priority.NEEDS);
        this.description = description;
        this.isRecurring = isRecurring;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Integer getDueDayOfMonth() {
        return dueDayOfMonth;
    }

    public void setDueDayOfMonth(Integer dueDayOfMonth) {
        this.dueDayOfMonth = dueDayOfMonth;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
