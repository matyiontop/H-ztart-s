package hu.haztartas.dto;

import java.math.BigDecimal;

public class CashFlowSummaryDto {
    private BigDecimal monthlyIncome = BigDecimal.ZERO;
    private BigDecimal monthlyFixedExpenses = BigDecimal.ZERO;
    private BigDecimal monthlyVariableExpenses = BigDecimal.ZERO;
    private BigDecimal totalMonthlyExpenses = BigDecimal.ZERO;
    private BigDecimal netMonthlySavings = BigDecimal.ZERO;
    private double savingsRatePercent;

    private BigDecimal annualIncome = BigDecimal.ZERO;
    private BigDecimal annualExpenses = BigDecimal.ZERO;
    private BigDecimal annualSavings = BigDecimal.ZERO;

    public CashFlowSummaryDto() {}

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyFixedExpenses() {
        return monthlyFixedExpenses;
    }

    public void setMonthlyFixedExpenses(BigDecimal monthlyFixedExpenses) {
        this.monthlyFixedExpenses = monthlyFixedExpenses;
    }

    public BigDecimal getMonthlyVariableExpenses() {
        return monthlyVariableExpenses;
    }

    public void setMonthlyVariableExpenses(BigDecimal monthlyVariableExpenses) {
        this.monthlyVariableExpenses = monthlyVariableExpenses;
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

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public BigDecimal getAnnualExpenses() {
        return annualExpenses;
    }

    public void setAnnualExpenses(BigDecimal annualExpenses) {
        this.annualExpenses = annualExpenses;
    }

    public BigDecimal getAnnualSavings() {
        return annualSavings;
    }

    public void setAnnualSavings(BigDecimal annualSavings) {
        this.annualSavings = annualSavings;
    }
}
