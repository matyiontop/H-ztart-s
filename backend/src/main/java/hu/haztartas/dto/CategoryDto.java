package hu.haztartas.dto;

import hu.haztartas.entity.enums.CategoryType;
import hu.haztartas.entity.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CategoryDto {
    private Long id;

    @NotBlank(message = "A kategória neve nem lehet üres")
    private String name;

    @NotNull(message = "A kategória típusa kötelező")
    private CategoryType type;

    private boolean isFixed;
    private String icon = "wallet";
    private String color = "#4f46e5";
    private BigDecimal monthlyBudgetLimit = BigDecimal.ZERO;
    private Priority priority = Priority.NEEDS;

    public CategoryDto() {}

    public CategoryDto(Long id, String name, CategoryType type, boolean isFixed, String icon, String color, BigDecimal monthlyBudgetLimit, Priority priority) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isFixed = isFixed;
        this.icon = icon;
        this.color = color;
        this.monthlyBudgetLimit = monthlyBudgetLimit != null ? monthlyBudgetLimit : BigDecimal.ZERO;
        this.priority = priority != null ? priority : Priority.NEEDS;
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
}
