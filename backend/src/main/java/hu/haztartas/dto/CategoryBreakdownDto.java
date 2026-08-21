package hu.haztartas.dto;

import java.math.BigDecimal;

public class CategoryBreakdownDto {
    private Long categoryId;
    private String categoryName;
    private String color;
    private String icon;
    private BigDecimal totalAmount;
    private double percentage;
    private BigDecimal budgetLimit;
    private boolean isOverBudget;

    public CategoryBreakdownDto() {}

    public CategoryBreakdownDto(Long categoryId, String categoryName, String color, String icon, BigDecimal totalAmount, double percentage, BigDecimal budgetLimit, boolean isOverBudget) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.color = color;
        this.icon = icon;
        this.totalAmount = totalAmount;
        this.percentage = percentage;
        this.budgetLimit = budgetLimit;
        this.isOverBudget = isOverBudget;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(BigDecimal budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    public boolean isOverBudget() {
        return isOverBudget;
    }

    public void setOverBudget(boolean overBudget) {
        isOverBudget = overBudget;
    }
}
