package hu.haztartas.dto;

import java.math.BigDecimal;

public class SimulationResultDto {
    // Alapértékek
    private BigDecimal baselineIncome = BigDecimal.ZERO;
    private BigDecimal baselineExpenses = BigDecimal.ZERO;
    private BigDecimal baselineSavings = BigDecimal.ZERO;
    private double baselineSavingsRate;

    // Szimulált értékek
    private BigDecimal simulatedIncome = BigDecimal.ZERO;
    private BigDecimal simulatedExpenses = BigDecimal.ZERO;
    private BigDecimal simulatedSavings = BigDecimal.ZERO;
    private double simulatedSavingsRate;

    // Különbségek (Delta)
    private BigDecimal incomeDifference = BigDecimal.ZERO;
    private BigDecimal expenseDifference = BigDecimal.ZERO;
    private BigDecimal savingsDifference = BigDecimal.ZERO;

    // Éves hatás
    private BigDecimal annualSavingsImpact = BigDecimal.ZERO;
    private String outcomeMessage;
    private boolean isPositiveImpact;

    public SimulationResultDto() {}

    public BigDecimal getBaselineIncome() {
        return baselineIncome;
    }

    public void setBaselineIncome(BigDecimal baselineIncome) {
        this.baselineIncome = baselineIncome;
    }

    public BigDecimal getBaselineExpenses() {
        return baselineExpenses;
    }

    public void setBaselineExpenses(BigDecimal baselineExpenses) {
        this.baselineExpenses = baselineExpenses;
    }

    public BigDecimal getBaselineSavings() {
        return baselineSavings;
    }

    public void setBaselineSavings(BigDecimal baselineSavings) {
        this.baselineSavings = baselineSavings;
    }

    public double getBaselineSavingsRate() {
        return baselineSavingsRate;
    }

    public void setBaselineSavingsRate(double baselineSavingsRate) {
        this.baselineSavingsRate = baselineSavingsRate;
    }

    public BigDecimal getSimulatedIncome() {
        return simulatedIncome;
    }

    public void setSimulatedIncome(BigDecimal simulatedIncome) {
        this.simulatedIncome = simulatedIncome;
    }

    public BigDecimal getSimulatedExpenses() {
        return simulatedExpenses;
    }

    public void setSimulatedExpenses(BigDecimal simulatedExpenses) {
        this.simulatedExpenses = simulatedExpenses;
    }

    public BigDecimal getSimulatedSavings() {
        return simulatedSavings;
    }

    public void setSimulatedSavings(BigDecimal simulatedSavings) {
        this.simulatedSavings = simulatedSavings;
    }

    public double getSimulatedSavingsRate() {
        return simulatedSavingsRate;
    }

    public void setSimulatedSavingsRate(double simulatedSavingsRate) {
        this.simulatedSavingsRate = simulatedSavingsRate;
    }

    public BigDecimal getIncomeDifference() {
        return incomeDifference;
    }

    public void setIncomeDifference(BigDecimal incomeDifference) {
        this.incomeDifference = incomeDifference;
    }

    public BigDecimal getExpenseDifference() {
        return expenseDifference;
    }

    public void setExpenseDifference(BigDecimal expenseDifference) {
        this.expenseDifference = expenseDifference;
    }

    public BigDecimal getSavingsDifference() {
        return savingsDifference;
    }

    public void setSavingsDifference(BigDecimal savingsDifference) {
        this.savingsDifference = savingsDifference;
    }

    public BigDecimal getAnnualSavingsImpact() {
        return annualSavingsImpact;
    }

    public void setAnnualSavingsImpact(BigDecimal annualSavingsImpact) {
        this.annualSavingsImpact = annualSavingsImpact;
    }

    public String getOutcomeMessage() {
        return outcomeMessage;
    }

    public void setOutcomeMessage(String outcomeMessage) {
        this.outcomeMessage = outcomeMessage;
    }

    public boolean isPositiveImpact() {
        return isPositiveImpact;
    }

    public void setPositiveImpact(boolean positiveImpact) {
        isPositiveImpact = positiveImpact;
    }
}
