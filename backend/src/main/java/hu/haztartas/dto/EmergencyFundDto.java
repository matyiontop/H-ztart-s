package hu.haztartas.dto;

import java.math.BigDecimal;

public class EmergencyFundDto {
    private BigDecimal monthlyFixedExpenses = BigDecimal.ZERO;
    private BigDecimal monthlyEssentialExpenses = BigDecimal.ZERO; // Fix + alapvető élelmiszer/gyógyszer

    // 3, 6, 12 havi célösszegek
    private BigDecimal targetThreeMonths = BigDecimal.ZERO;
    private BigDecimal targetSixMonths = BigDecimal.ZERO;
    private BigDecimal targetTwelveMonths = BigDecimal.ZERO;

    private BigDecimal currentSavedAmount = BigDecimal.ZERO;
    private double currentThreeMonthsProgress;
    private double currentSixMonthsProgress;
    private double currentTwelveMonthsProgress;

    private BigDecimal monthlySavingsCapacity = BigDecimal.ZERO;
    private int monthsToReachThreeMonths;
    private int monthsToReachSixMonths;

    private String advice;

    public EmergencyFundDto() {}

    public BigDecimal getMonthlyFixedExpenses() {
        return monthlyFixedExpenses;
    }

    public void setMonthlyFixedExpenses(BigDecimal monthlyFixedExpenses) {
        this.monthlyFixedExpenses = monthlyFixedExpenses;
    }

    public BigDecimal getMonthlyEssentialExpenses() {
        return monthlyEssentialExpenses;
    }

    public void setMonthlyEssentialExpenses(BigDecimal monthlyEssentialExpenses) {
        this.monthlyEssentialExpenses = monthlyEssentialExpenses;
    }

    public BigDecimal getTargetThreeMonths() {
        return targetThreeMonths;
    }

    public void setTargetThreeMonths(BigDecimal targetThreeMonths) {
        this.targetThreeMonths = targetThreeMonths;
    }

    public BigDecimal getTargetSixMonths() {
        return targetSixMonths;
    }

    public void setTargetSixMonths(BigDecimal targetSixMonths) {
        this.targetSixMonths = targetSixMonths;
    }

    public BigDecimal getTargetTwelveMonths() {
        return targetTwelveMonths;
    }

    public void setTargetTwelveMonths(BigDecimal targetTwelveMonths) {
        this.targetTwelveMonths = targetTwelveMonths;
    }

    public BigDecimal getCurrentSavedAmount() {
        return currentSavedAmount;
    }

    public void setCurrentSavedAmount(BigDecimal currentSavedAmount) {
        this.currentSavedAmount = currentSavedAmount;
    }

    public double getCurrentThreeMonthsProgress() {
        return currentThreeMonthsProgress;
    }

    public void setCurrentThreeMonthsProgress(double currentThreeMonthsProgress) {
        this.currentThreeMonthsProgress = currentThreeMonthsProgress;
    }

    public double getCurrentSixMonthsProgress() {
        return currentSixMonthsProgress;
    }

    public void setCurrentSixMonthsProgress(double currentSixMonthsProgress) {
        this.currentSixMonthsProgress = currentSixMonthsProgress;
    }

    public double getCurrentTwelveMonthsProgress() {
        return currentTwelveMonthsProgress;
    }

    public void setCurrentTwelveMonthsProgress(double currentTwelveMonthsProgress) {
        this.currentTwelveMonthsProgress = currentTwelveMonthsProgress;
    }

    public BigDecimal getMonthlySavingsCapacity() {
        return monthlySavingsCapacity;
    }

    public void setMonthlySavingsCapacity(BigDecimal monthlySavingsCapacity) {
        this.monthlySavingsCapacity = monthlySavingsCapacity;
    }

    public int getMonthsToReachThreeMonths() {
        return monthsToReachThreeMonths;
    }

    public void setMonthsToReachThreeMonths(int monthsToReachThreeMonths) {
        this.monthsToReachThreeMonths = monthsToReachThreeMonths;
    }

    public int getMonthsToReachSixMonths() {
        return monthsToReachSixMonths;
    }

    public void setMonthsToReachSixMonths(int monthsToReachSixMonths) {
        this.monthsToReachSixMonths = monthsToReachSixMonths;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
