package hu.haztartas.entity;

import hu.haztartas.entity.enums.CategoryType;
import hu.haztartas.entity.enums.Priority;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryType type;

    @Column(name = "is_fixed", nullable = false)
    private boolean isFixed = false;

    @Column(length = 50)
    private String icon = "wallet";

    @Column(length = 20)
    private String color = "#4f46e5";

    @Column(name = "monthly_budget_limit", precision = 14, scale = 2)
    private BigDecimal monthlyBudgetLimit = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Priority priority = Priority.NEEDS;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Category() {}

    public Category(String name, CategoryType type, boolean isFixed, String icon, String color, BigDecimal monthlyBudgetLimit, Priority priority) {
        this.name = name;
        this.type = type;
        this.isFixed = isFixed;
        this.icon = icon;
        this.color = color;
        this.monthlyBudgetLimit = monthlyBudgetLimit != null ? monthlyBudgetLimit : BigDecimal.ZERO;
        this.priority = priority != null ? priority : Priority.NEEDS;
        this.createdAt = OffsetDateTime.now();
    }

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

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getMonthlyBudgetLimit() {
        return monthlyBudgetLimit;
    }

    public void setMonthlyBudgetLimit(BigDecimal monthlyBudgetLimit) {
        this.monthlyBudgetLimit = monthlyBudgetLimit;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
