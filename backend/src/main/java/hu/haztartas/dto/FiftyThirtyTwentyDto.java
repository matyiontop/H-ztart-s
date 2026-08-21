package hu.haztartas.dto;

import java.math.BigDecimal;

public class FiftyThirtyTwentyDto {
    private BigDecimal monthlyIncome = BigDecimal.ZERO;

    // 50% Szükségletek (Needs)
    private BigDecimal actualNeeds = BigDecimal.ZERO;
    private double actualNeedsPercent;
    private BigDecimal targetNeeds = BigDecimal.ZERO;
    private BigDecimal needsDifference = BigDecimal.ZERO;

    // 30% Vágyak / Igények (Wants)
    private BigDecimal actualWants = BigDecimal.ZERO;
    private double actualWantsPercent;
    private BigDecimal targetWants = BigDecimal.ZERO;
    private BigDecimal wantsDifference = BigDecimal.ZERO;

    // 20% Megtakarítás (Savings)
    private BigDecimal actualSavings = BigDecimal.ZERO;
    private double actualSavingsPercent;
    private BigDecimal targetSavings = BigDecimal.ZERO;
    private BigDecimal savingsDifference = BigDecimal.ZERO;

    private String evaluationSummary;

    public FiftyThirtyTwentyDto() {}

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getActualNeeds() {
        return actualNeeds;
    }

    public void setActualNeeds(BigDecimal actualNeeds) {
        this.actualNeeds = actualNeeds;
    }

    public double getActualNeedsPercent() {
        return actualNeedsPercent;
    }

    public void setActualNeedsPercent(double actualNeedsPercent) {
        this.actualNeedsPercent = actualNeedsPercent;
    }

    public BigDecimal getTargetNeeds() {
        return targetNeeds;
    }

    public void setTargetNeeds(BigDecimal targetNeeds) {
        this.targetNeeds = targetNeeds;
    }

    public BigDecimal getNeedsDifference() {
        return needsDifference;
    }

    public void setNeedsDifference(BigDecimal needsDifference) {
        this.needsDifference = needsDifference;
    }

    public BigDecimal getActualWants() {
        return actualWants;
    }

    public void setActualWants(BigDecimal actualWants) {
        this.actualWants = actualWants;
    }

    public double getActualWantsPercent() {
        return actualWantsPercent;
    }

    public void setActualWantsPercent(double actualWantsPercent) {
        this.actualWantsPercent = actualWantsPercent;
    }

    public BigDecimal getTargetWants() {
        return targetWants;
    }

    public void setTargetWants(BigDecimal targetWants) {
        this.targetWants = targetWants;
    }

    public BigDecimal getWantsDifference() {
        return wantsDifference;
    }

    public void setWantsDifference(BigDecimal wantsDifference) {
        this.wantsDifference = wantsDifference;
    }

    public BigDecimal getActualSavings() {
        return actualSavings;
    }

    public void setActualSavings(BigDecimal actualSavings) {
        this.actualSavings = actualSavings;
    }

    public double getActualSavingsPercent() {
        return actualSavingsPercent;
    }

    public void setActualSavingsPercent(double actualSavingsPercent) {
        this.actualSavingsPercent = actualSavingsPercent;
    }

    public BigDecimal getTargetSavings() {
        return targetSavings;
    }

    public void setTargetSavings(BigDecimal targetSavings) {
        this.targetSavings = targetSavings;
    }

    public BigDecimal getSavingsDifference() {
        return savingsDifference;
    }

    public void setSavingsDifference(BigDecimal savingsDifference) {
        this.savingsDifference = savingsDifference;
    }

    public String getEvaluationSummary() {
        return evaluationSummary;
    }

    public void setEvaluationSummary(String evaluationSummary) {
        this.evaluationSummary = evaluationSummary;
    }
}
