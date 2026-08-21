package hu.haztartas.dto;

import java.math.BigDecimal;

public class MonthlyTrendDto {
    private String monthName;
    private int year;
    private int month;
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalFixedExpenses = BigDecimal.ZERO;
    private BigDecimal totalVariableExpenses = BigDecimal.ZERO;
    private BigDecimal totalExpenses = BigDecimal.ZERO;
    private BigDecimal netSavings = BigDecimal.ZERO;

    public MonthlyTrendDto() {}

    public MonthlyTrendDto(String monthName, int year, int month, BigDecimal totalIncome, BigDecimal totalFixedExpenses, BigDecimal totalVariableExpenses, BigDecimal totalExpenses, BigDecimal netSavings) {
        this.monthName = monthName;
        this.year = year;
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalFixedExpenses = totalFixedExpenses;
        this.totalVariableExpenses = totalVariableExpenses;
        this.totalExpenses = totalExpenses;
        this.netSavings = netSavings;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalFixedExpenses() {
        return totalFixedExpenses;
    }

    public void setTotalFixedExpenses(BigDecimal totalFixedExpenses) {
        this.totalFixedExpenses = totalFixedExpenses;
    }

    public BigDecimal getTotalVariableExpenses() {
        return totalVariableExpenses;
    }

    public void setTotalVariableExpenses(BigDecimal totalVariableExpenses) {
        this.totalVariableExpenses = totalVariableExpenses;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(BigDecimal netSavings) {
        this.netSavings = netSavings;
    }
}
