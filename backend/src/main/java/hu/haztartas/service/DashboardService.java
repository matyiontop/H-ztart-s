package hu.haztartas.service;

import hu.haztartas.dto.*;
import hu.haztartas.repository.CategoryRepository;
import hu.haztartas.repository.ExpenseRepository;
import hu.haztartas.repository.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final CalculationService calculationService;
    private final AccountBalanceService accountBalanceService;

    public DashboardService(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository,
            IncomeService incomeService,
            ExpenseService expenseService,
            CalculationService calculationService,
            AccountBalanceService accountBalanceService
    ) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.calculationService = calculationService;
        this.accountBalanceService = accountBalanceService;
    }

    public DashboardSummaryDto getDashboardSummary() {
        CashFlowSummaryDto cashFlow = calculationService.calculateCashFlow();
        AccountBalanceDto balanceDto = accountBalanceService.getBalanceDto();

        DashboardSummaryDto summary = new DashboardSummaryDto();
        summary.setCurrentBalance(balanceDto.getBalance());
        summary.setBankAmount(balanceDto.getBankAmount());
        summary.setCashAmount(balanceDto.getCashAmount());
        summary.setProjectedEndOfMonthBalance(balanceDto.getProjectedEndOfMonthBalance());

        summary.setTotalMonthlyIncome(cashFlow.getMonthlyIncome());
        summary.setTotalMonthlyFixedExpenses(cashFlow.getMonthlyFixedExpenses());
        summary.setTotalMonthlyVariableExpenses(cashFlow.getMonthlyVariableExpenses());
        summary.setTotalMonthlyExpenses(cashFlow.getTotalMonthlyExpenses());
        summary.setNetMonthlySavings(cashFlow.getNetMonthlySavings());
        summary.setSavingsRatePercent(cashFlow.getSavingsRatePercent());

        // 1. Kategória megoszlás a valós rögzített kiadások alapján
        List<Object[]> catData = expenseRepository.findCategoryExpensesTotal();
        List<CategoryBreakdownDto> breakdownList = new ArrayList<>();
        BigDecimal totalExpenses = cashFlow.getTotalMonthlyExpenses();

        for (Object[] row : catData) {
            Long catId = row[0] instanceof Number ? ((Number) row[0]).longValue() : null;
            String name = (String) row[1];
            String color = (String) row[2];
            String icon = (String) row[3];
            BigDecimal amount = (BigDecimal) row[4];

            if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
                double pct = 0.0;
                if (totalExpenses.compareTo(BigDecimal.ZERO) > 0) {
                    pct = amount.divide(totalExpenses, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
                    pct = Math.round(pct * 10.0) / 10.0;
                }

                BigDecimal budgetLimit = (catId != null && catId > 0) ? categoryRepository.findById(catId)
                        .map(c -> c.getMonthlyBudgetLimit())
                        .orElse(BigDecimal.ZERO) : BigDecimal.ZERO;

                boolean isOver = budgetLimit.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(budgetLimit) > 0;

                breakdownList.add(new CategoryBreakdownDto(catId, name, color, icon, amount, pct, budgetLimit, isOver));
            }
        }

        summary.setExpenseCategories(breakdownList);

        // 2. Havi trend
        YearMonth now = YearMonth.now();
        List<MonthlyTrendDto> trends = new ArrayList<>();
        Locale huLocale = Locale.of("hu", "HU");
        for (int i = 5; i >= 0; i--) {
            YearMonth targetMonth = now.minusMonths(i);
            LocalDate start = targetMonth.atDay(1);
            LocalDate end = targetMonth.atEndOfMonth();

            BigDecimal inc = incomeRepository.sumAmountBetweenDates(start, end);
            if (inc.compareTo(BigDecimal.ZERO) == 0 && i == 0) {
                inc = cashFlow.getMonthlyIncome();
            }

            BigDecimal fixExp = cashFlow.getMonthlyFixedExpenses();
            BigDecimal varExp = expenseRepository.sumVariableExpensesBetweenDates(start, end);
            if (varExp.compareTo(BigDecimal.ZERO) == 0 && i == 0) {
                varExp = cashFlow.getMonthlyVariableExpenses();
            }

            BigDecimal totalExp = fixExp.add(varExp);
            BigDecimal net = inc.subtract(totalExp);

            String monthName = targetMonth.getMonth().getDisplayName(TextStyle.SHORT, huLocale);

            trends.add(new MonthlyTrendDto(
                    monthName,
                    targetMonth.getYear(),
                    targetMonth.getMonthValue(),
                    inc,
                    fixExp,
                    varExp,
                    totalExp,
                    net
            ));
        }
        summary.setMonthlyTrends(trends);

        // 3. Legutóbbi tételek
        summary.setRecentIncomes(incomeService.getAllIncomes().stream().limit(5).toList());
        summary.setRecentExpenses(expenseService.getAllExpenses().stream().limit(6).toList());

        return summary;
    }
}
