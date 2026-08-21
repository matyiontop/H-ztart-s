package hu.haztartas.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DashboardSummaryDto {
    private BigDecimal currentBalance = BigDecimal.ZERO;
    private BigDecimal bankAmount = BigDecimal.ZERO;
    private BigDecimal cashAmount = BigDecimal.ZERO;
    private BigDecimal projectedEndOfMonthBalance = BigDecimal.ZERO;

    private BigDecimal totalMonthlyIncome = BigDecimal.ZERO;
    private BigDecimal totalMonthlyFixedExpenses = BigDecimal.ZERO;
    private BigDecimal totalMonthlyVariableExpenses = BigDecimal.ZERO;
    private BigDecimal totalMonthlyExpenses = BigDecimal.ZERO;
    private BigDecimal netMonthlySavings = BigDecimal.ZERO;
    private double savingsRatePercent;

    private List<CategoryBreakdownDto> expenseCategories = new ArrayList<>();
    private List<MonthlyTrendDto> monthlyTrends = new ArrayList<>();
    private List<IncomeDto> recentIncomes = new ArrayList<>();
    private List<ExpenseDto> recentExpenses = new ArrayList<>();

    public DashboardSummaryDto() {}

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
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

    public BigDecimal getProjectedEndOfMonthBalance() {
        return projectedEndOfMonthBalance;
    }

    public void setProjectedEndOfMonthBalance(BigDecimal projectedEndOfMonthBalance) {
        this.projectedEndOfMonthBalance = projectedEndOfMonthBalance;
    }

    public BigDecimal getTotalMonthlyIncome() {
        return totalMonthlyIncome;
    }

    public void setTotalMonthlyIncome(BigDecimal totalMonthlyIncome) {
        this.totalMonthlyIncome = totalMonthlyIncome;
    }

    public BigDecimal getTotalMonthlyFixedExpenses() {
        return totalMonthlyFixedExpenses;
    }

    public void setTotalMonthlyFixedExpenses(BigDecimal totalMonthlyFixedExpenses) {
        this.totalMonthlyFixedExpenses = totalMonthlyFixedExpenses;
    }

    public BigDecimal getTotalMonthlyVariableExpenses() {
        return totalMonthlyVariableExpenses;
    }

    public void setTotalMonthlyVariableExpenses(BigDecimal totalMonthlyVariableExpenses) {
        this.totalMonthlyVariableExpenses = totalMonthlyVariableExpenses;
    }

    public BigDecimal getTotalMonthlyExpenses() {
        return totalMonthlyExpenses;
    }

    public void setTotalMonthlyExpenses(BigDecimal totalMonthlyExpenses) {
        this.totalMonthlyExpenses = totalMonthlyExpenses;
    }

    public BigDecimal getNetMonthlySavings() {
        return netMonthlySavings;
    }

    public void setNetMonthlySavings(BigDecimal netMonthlySavings) {
        this.netMonthlySavings = netMonthlySavings;
    }

    public double getSavingsRatePercent() {
        return savingsRatePercent;
    }

    public void setSavingsRatePercent(double savingsRatePercent) {
        this.savingsRatePercent = savingsRatePercent;
    }

    public List<CategoryBreakdownDto> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(List<CategoryBreakdownDto> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    public List<MonthlyTrendDto> getMonthlyTrends() {
        return monthlyTrends;
    }

    public void setMonthlyTrends(List<MonthlyTrendDto> monthlyTrends) {
        this.monthlyTrends = monthlyTrends;
    }

    public List<IncomeDto> getRecentIncomes() {
        return recentIncomes;
    }

    public void setRecentIncomes(List<IncomeDto> recentIncomes) {
        this.recentIncomes = recentIncomes;
    }

    public List<ExpenseDto> getRecentExpenses() {
        return recentExpenses;
    }

    public void setRecentExpenses(List<ExpenseDto> recentExpenses) {
        this.recentExpenses = recentExpenses;
    }
}
