package hu.haztartas.dto;

import java.math.BigDecimal;

public class SimulationRequestDto {
    // Jövedelem változás %-ban (pl. +10% fizetésemelés vagy -20% bevételkiesés)
    private double incomeChangePercent = 0.0;

    // Fix rezsi / lakhatási költség változás %-ban (pl. +15% rezsiárnövekedés)
    private double fixedExpenseChangePercent = 0.0;

    // Változó kiadások lefaragása %-ban (pl. -15% spórolás élelmiszeren/szórakozáson)
    private double variableExpenseChangePercent = 0.0;

    // Tervezett új havi kiadás (pl. új hiteltörlesztő vagy lízing)
    private BigDecimal additionalMonthlyExpense = BigDecimal.ZERO;

    // Tervezett új havi bevétel (pl. új mellékállás)
    private BigDecimal additionalMonthlyIncome = BigDecimal.ZERO;

    // Egyszeri nagy kiadás (pl. autójavítás vagy hűtőcsere)
    private BigDecimal oneTimeExpense = BigDecimal.ZERO;

    public SimulationRequestDto() {}

    public double getIncomeChangePercent() {
        return incomeChangePercent;
    }

    public void setIncomeChangePercent(double incomeChangePercent) {
        this.incomeChangePercent = incomeChangePercent;
    }

    public double getFixedExpenseChangePercent() {
        return fixedExpenseChangePercent;
    }

    public void setFixedExpenseChangePercent(double fixedExpenseChangePercent) {
        this.fixedExpenseChangePercent = fixedExpenseChangePercent;
    }

    public double getVariableExpenseChangePercent() {
        return variableExpenseChangePercent;
    }

    public void setVariableExpenseChangePercent(double variableExpenseChangePercent) {
        this.variableExpenseChangePercent = variableExpenseChangePercent;
    }

    public BigDecimal getAdditionalMonthlyExpense() {
        return additionalMonthlyExpense;
    }

    public void setAdditionalMonthlyExpense(BigDecimal additionalMonthlyExpense) {
        this.additionalMonthlyExpense = additionalMonthlyExpense;
    }

    public BigDecimal getAdditionalMonthlyIncome() {
        return additionalMonthlyIncome;
    }

    public void setAdditionalMonthlyIncome(BigDecimal additionalMonthlyIncome) {
        this.additionalMonthlyIncome = additionalMonthlyIncome;
    }

    public BigDecimal getOneTimeExpense() {
        return oneTimeExpense;
    }

    public void setOneTimeExpense(BigDecimal oneTimeExpense) {
        this.oneTimeExpense = oneTimeExpense;
    }
}
