package hu.haztartas.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BalanceTrajectoryDto {
    private BigDecimal currentActualBalance = BigDecimal.ZERO;
    private BigDecimal currentCalculatedBalance = BigDecimal.ZERO;
    private BigDecimal difference = BigDecimal.ZERO;
    private List<BalancePointDto> monthlyPoints = new ArrayList<>();
    private List<BalancePointDto> dailyPoints = new ArrayList<>();

    public BalanceTrajectoryDto() {}

    public BigDecimal getCurrentActualBalance() {
        return currentActualBalance;
    }

    public void setCurrentActualBalance(BigDecimal currentActualBalance) {
        this.currentActualBalance = currentActualBalance;
    }

    public BigDecimal getCurrentCalculatedBalance() {
        return currentCalculatedBalance;
    }

    public void setCurrentCalculatedBalance(BigDecimal currentCalculatedBalance) {
        this.currentCalculatedBalance = currentCalculatedBalance;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public List<BalancePointDto> getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(List<BalancePointDto> monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    public List<BalancePointDto> getDailyPoints() {
        return dailyPoints;
    }

    public void setDailyPoints(List<BalancePointDto> dailyPoints) {
        this.dailyPoints = dailyPoints;
    }
}
