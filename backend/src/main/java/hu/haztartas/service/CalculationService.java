package hu.haztartas.service;

import hu.haztartas.dto.*;
import hu.haztartas.repository.ExpenseRepository;
import hu.haztartas.repository.IncomeRepository;
import hu.haztartas.repository.SavingsGoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@Transactional(readOnly = true)
public class CalculationService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public CalculationService(IncomeRepository incomeRepository, ExpenseRepository expenseRepository, SavingsGoalRepository savingsGoalRepository) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    /**
     * Havi és éves pénzforgalom (Cash Flow) kalkuláció
     */
    public CashFlowSummaryDto calculateCashFlow() {
        YearMonth now = YearMonth.now();
        LocalDate startOfMonth = now.atDay(1);
        LocalDate endOfMonth = now.atEndOfMonth();

        // Ha a jelenlegi hónapra van tétel, azt számoljuk, különben az összes rögzített havi tételt
        BigDecimal monthlyIncome = incomeRepository.sumAmountBetweenDates(startOfMonth, endOfMonth);
        if (monthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            monthlyIncome = incomeRepository.sumMonthlyRecurringAmount();
        }
        if (monthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            monthlyIncome = incomeRepository.sumTotalIncomes();
        }

        BigDecimal fixedExpenses = expenseRepository.sumTotalFixedExpenses();
        BigDecimal variableExpenses = expenseRepository.sumVariableExpensesBetweenDates(startOfMonth, endOfMonth);
        if (variableExpenses.compareTo(BigDecimal.ZERO) == 0) {
            variableExpenses = expenseRepository.sumTotalVariableExpenses();
        }

        BigDecimal totalExpenses = fixedExpenses.add(variableExpenses);
        BigDecimal netSavings = monthlyIncome.subtract(totalExpenses);

        double savingsRate = 0.0;
        if (monthlyIncome.compareTo(BigDecimal.ZERO) > 0) {
            savingsRate = netSavings.divide(monthlyIncome, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
            savingsRate = Math.round(savingsRate * 10.0) / 10.0;
        }

        CashFlowSummaryDto dto = new CashFlowSummaryDto();
        dto.setMonthlyIncome(monthlyIncome);
        dto.setMonthlyFixedExpenses(fixedExpenses);
        dto.setMonthlyVariableExpenses(variableExpenses);
        dto.setTotalMonthlyExpenses(totalExpenses);
        dto.setNetMonthlySavings(netSavings);
        dto.setSavingsRatePercent(savingsRate);

        dto.setAnnualIncome(monthlyIncome.multiply(BigDecimal.valueOf(12)));
        dto.setAnnualExpenses(totalExpenses.multiply(BigDecimal.valueOf(12)));
        dto.setAnnualSavings(netSavings.multiply(BigDecimal.valueOf(12)));

        return dto;
    }

    /**
     * 50 / 30 / 20 Aranyszabály elemzés
     */
    public FiftyThirtyTwentyDto calculate503020() {
        CashFlowSummaryDto cashFlow = calculateCashFlow();
        BigDecimal income = cashFlow.getMonthlyIncome();
        BigDecimal actualNeeds = cashFlow.getMonthlyFixedExpenses();
        BigDecimal actualWants = cashFlow.getMonthlyVariableExpenses();
        BigDecimal actualSavings = cashFlow.getNetMonthlySavings().max(BigDecimal.ZERO);

        FiftyThirtyTwentyDto dto = new FiftyThirtyTwentyDto();
        dto.setMonthlyIncome(income);
        dto.setActualNeeds(actualNeeds);
        dto.setActualWants(actualWants);
        dto.setActualSavings(actualSavings);

        if (income.compareTo(BigDecimal.ZERO) > 0) {
            dto.setTargetNeeds(income.multiply(BigDecimal.valueOf(0.50)).setScale(0, RoundingMode.HALF_UP));
            dto.setTargetWants(income.multiply(BigDecimal.valueOf(0.30)).setScale(0, RoundingMode.HALF_UP));
            dto.setTargetSavings(income.multiply(BigDecimal.valueOf(0.20)).setScale(0, RoundingMode.HALF_UP));

            double needsPct = actualNeeds.divide(income, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
            double wantsPct = actualWants.divide(income, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
            double savingsPct = actualSavings.divide(income, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;

            dto.setActualNeedsPercent(Math.round(needsPct * 10.0) / 10.0);
            dto.setActualWantsPercent(Math.round(wantsPct * 10.0) / 10.0);
            dto.setActualSavingsPercent(Math.round(savingsPct * 10.0) / 10.0);

            dto.setNeedsDifference(actualNeeds.subtract(dto.getTargetNeeds()));
            dto.setWantsDifference(actualWants.subtract(dto.getTargetWants()));
            dto.setSavingsDifference(actualSavings.subtract(dto.getTargetSavings()));
            dto.setEvaluationSummary("Pénzügyi mérleg elemzés");
        } else {
            dto.setEvaluationSummary("Rögzíts havi jövedelmet!");
        }

        return dto;
    }

    /**
     * Vésztartalék Kalkuláció
     */
    public EmergencyFundDto calculateEmergencyFund() {
        CashFlowSummaryDto cashFlow = calculateCashFlow();
        BigDecimal fixed = cashFlow.getMonthlyFixedExpenses();
        BigDecimal essential = fixed.add(cashFlow.getMonthlyVariableExpenses().multiply(BigDecimal.valueOf(0.60))).setScale(0, RoundingMode.HALF_UP);
        if (essential.compareTo(BigDecimal.ZERO) == 0) {
            essential = BigDecimal.valueOf(150000);
        }

        BigDecimal saved = savingsGoalRepository.sumTotalSavedAmount();

        EmergencyFundDto dto = new EmergencyFundDto();
        dto.setMonthlyFixedExpenses(fixed);
        dto.setMonthlyEssentialExpenses(essential);

        dto.setTargetThreeMonths(essential.multiply(BigDecimal.valueOf(3)));
        dto.setTargetSixMonths(essential.multiply(BigDecimal.valueOf(6)));
        dto.setTargetTwelveMonths(essential.multiply(BigDecimal.valueOf(12)));
        dto.setCurrentSavedAmount(saved);

        BigDecimal capacity = cashFlow.getNetMonthlySavings().max(BigDecimal.ZERO);
        dto.setMonthlySavingsCapacity(capacity);

        return dto;
    }

    /**
     * "Mi lenne, ha...?" (What-If) Szimulátor
     */
    public SimulationResultDto runSimulation(SimulationRequestDto req) {
        CashFlowSummaryDto base = calculateCashFlow();

        BigDecimal baseIncome = base.getMonthlyIncome();
        BigDecimal baseFixed = base.getMonthlyFixedExpenses();
        BigDecimal baseVariable = base.getMonthlyVariableExpenses();
        BigDecimal baseExpenses = base.getTotalMonthlyExpenses();
        BigDecimal baseSavings = base.getNetMonthlySavings();

        BigDecimal simIncome = baseIncome
                .multiply(BigDecimal.valueOf(1.0 + (req.getIncomeChangePercent() / 100.0)))
                .add(req.getAdditionalMonthlyIncome() != null ? req.getAdditionalMonthlyIncome() : BigDecimal.ZERO);

        BigDecimal simFixed = baseFixed
                .multiply(BigDecimal.valueOf(1.0 + (req.getFixedExpenseChangePercent() / 100.0)));

        BigDecimal simVariable = baseVariable
                .multiply(BigDecimal.valueOf(1.0 + (req.getVariableExpenseChangePercent() / 100.0)));

        BigDecimal simExpenses = simFixed.add(simVariable)
                .add(req.getAdditionalMonthlyExpense() != null ? req.getAdditionalMonthlyExpense() : BigDecimal.ZERO);

        BigDecimal simSavings = simIncome.subtract(simExpenses);

        double baseSavingsRate = base.getSavingsRatePercent();
        double simSavingsRate = 0.0;
        if (simIncome.compareTo(BigDecimal.ZERO) > 0) {
            simSavingsRate = simSavings.divide(simIncome, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
            simSavingsRate = Math.round(simSavingsRate * 10.0) / 10.0;
        }

        BigDecimal incomeDiff = simIncome.subtract(baseIncome);
        BigDecimal expenseDiff = simExpenses.subtract(baseExpenses);
        BigDecimal savingsDiff = simSavings.subtract(baseSavings);

        BigDecimal oneTime = req.getOneTimeExpense() != null ? req.getOneTimeExpense() : BigDecimal.ZERO;
        BigDecimal annualImpact = savingsDiff.multiply(BigDecimal.valueOf(12)).subtract(oneTime);

        SimulationResultDto res = new SimulationResultDto();
        res.setBaselineIncome(baseIncome);
        res.setBaselineExpenses(baseExpenses);
        res.setBaselineSavings(baseSavings);
        res.setBaselineSavingsRate(baseSavingsRate);

        res.setSimulatedIncome(simIncome.setScale(0, RoundingMode.HALF_UP));
        res.setSimulatedExpenses(simExpenses.setScale(0, RoundingMode.HALF_UP));
        res.setSimulatedSavings(simSavings.setScale(0, RoundingMode.HALF_UP));
        res.setSimulatedSavingsRate(simSavingsRate);

        res.setIncomeDifference(incomeDiff.setScale(0, RoundingMode.HALF_UP));
        res.setExpenseDifference(expenseDiff.setScale(0, RoundingMode.HALF_UP));
        res.setSavingsDifference(savingsDiff.setScale(0, RoundingMode.HALF_UP));
        res.setAnnualSavingsImpact(annualImpact.setScale(0, RoundingMode.HALF_UP));

        boolean isPositive = annualImpact.compareTo(BigDecimal.ZERO) >= 0;
        res.setPositiveImpact(isPositive);

        if (isPositive) {
            res.setOutcomeMessage("Pozitív forgatókönyv: éves szinten +" +
                    annualImpact.toPlainString() + " Ft többlet megtakarítás érhető el!");
        } else {
            res.setOutcomeMessage("Figyelem: a forgatókönyv havi " +
                    savingsDiff.abs().toPlainString() + " Ft-tal csökkenti a megtakarítást, éves hatása " +
                    annualImpact.toPlainString() + " Ft.");
        }

        return res;
    }
}
