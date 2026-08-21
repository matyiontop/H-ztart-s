package hu.haztartas.service;

import hu.haztartas.dto.BalancePointDto;
import hu.haztartas.dto.BalanceTrajectoryDto;
import hu.haztartas.dto.CashFlowSummaryDto;
import hu.haztartas.entity.AccountBalance;
import hu.haztartas.entity.BalanceHistory;
import hu.haztartas.entity.Expense;
import hu.haztartas.entity.Income;
import hu.haztartas.repository.AccountBalanceRepository;
import hu.haztartas.repository.BalanceHistoryRepository;
import hu.haztartas.repository.ExpenseRepository;
import hu.haztartas.repository.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Transactional
public class BalanceTrackingService {

    private final BalanceHistoryRepository balanceHistoryRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final CalculationService calculationService;

    public BalanceTrackingService(
            BalanceHistoryRepository balanceHistoryRepository,
            AccountBalanceRepository accountBalanceRepository,
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            CalculationService calculationService
    ) {
        this.balanceHistoryRepository = balanceHistoryRepository;
        this.accountBalanceRepository = accountBalanceRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.calculationService = calculationService;
    }

    public void recordSnapshot(LocalDate date, BigDecimal actualBalance, String note) {
        if (date == null) date = LocalDate.now();
        if (actualBalance == null) actualBalance = BigDecimal.ZERO;

        CashFlowSummaryDto cashFlow = calculationService.calculateCashFlow();
        BigDecimal calculated = actualBalance.add(cashFlow.getNetMonthlySavings());

        BalanceHistory history = new BalanceHistory(date, actualBalance, calculated, note);
        balanceHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public BalanceTrajectoryDto getTrajectory() {
        AccountBalance currentAcc = accountBalanceRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> new AccountBalance(BigDecimal.valueOf(150000), BigDecimal.valueOf(140000), BigDecimal.valueOf(10000), "Kezdő"));

        CashFlowSummaryDto cashFlow = calculationService.calculateCashFlow();
        BigDecimal currentActual = currentAcc.getBalance();
        BigDecimal netMonthlySavings = cashFlow.getNetMonthlySavings();
        BigDecimal currentCalculated = currentActual.add(netMonthlySavings);

        BalanceTrajectoryDto trajectory = new BalanceTrajectoryDto();
        trajectory.setCurrentActualBalance(currentActual);
        trajectory.setCurrentCalculatedBalance(currentCalculated);
        trajectory.setDifference(currentActual.subtract(currentCalculated));

        // Valós múltbeli rögzítések felolvasása az adatbázisból (kizárólag valós adatok)
        List<BalanceHistory> realHistory = balanceHistoryRepository.findAllByOrderByRecordDateAsc();
        Map<YearMonth, BigDecimal> realMonthlyMap = new HashMap<>();
        for (BalanceHistory h : realHistory) {
            if (h.getRecordDate() != null && h.getActualBalance() != null) {
                realMonthlyMap.put(YearMonth.from(h.getRecordDate()), h.getActualBalance());
            }
        }

        List<Income> allIncomes = incomeRepository.findAll();
        BigDecimal monthlyExpenses = cashFlow.getTotalMonthlyExpenses();

        YearMonth now = YearMonth.now();
        Locale huLocale = Locale.of("hu", "HU");
        List<BalancePointDto> monthlyPoints = new ArrayList<>();

        // 1. Múltbeli hónapok (ha van valós rögzített adat)
        for (int i = 6; i >= 1; i--) {
            YearMonth pastMonth = now.minusMonths(i);
            if (realMonthlyMap.containsKey(pastMonth)) {
                String monthName = pastMonth.getMonth().getDisplayName(TextStyle.SHORT, huLocale);
                String label = pastMonth.getYear() + ". " + monthName;
                BigDecimal pastCalc = currentActual.subtract(netMonthlySavings.multiply(BigDecimal.valueOf(i)));
                monthlyPoints.add(new BalancePointDto(label, pastMonth.atDay(1), realMonthlyMap.get(pastMonth), pastCalc));
            }
        }

        // 2. Jelenlegi hónap (valós és kalkulált adatpont)
        String currentMonthName = now.getMonth().getDisplayName(TextStyle.SHORT, huLocale);
        String currentLabel = now.getYear() + ". " + currentMonthName;
        monthlyPoints.add(new BalancePointDto(currentLabel, now.atDay(1), currentActual, currentActual));

        // 3. Jövőbeli hónapok pontos kalkulációja az érvényes durationMonths bevételek alapján
        int futureMonthsToProject = 6;
        BigDecimal runningCalculatedBalance = currentActual;

        for (int i = 1; i <= futureMonthsToProject; i++) {
            YearMonth futureMonth = now.plusMonths(i);
            String monthName = futureMonth.getMonth().getDisplayName(TextStyle.SHORT, huLocale);
            String label = futureMonth.getYear() + ". " + monthName;

            // Havi aktív bevételek összegzése erre a jövőbeli hónapra
            BigDecimal futureMonthIncome = BigDecimal.ZERO;
            for (Income inc : allIncomes) {
                YearMonth incStart = YearMonth.from(inc.getReceivedDate() != null ? inc.getReceivedDate() : LocalDate.now());
                Integer duration = inc.getDurationMonths();

                boolean isActiveInMonth;
                if (!inc.isRecurring() || (duration != null && duration == 1)) {
                    // Egyszeri bevétel: csak az induló hónapjában érvényes
                    isActiveInMonth = incStart.equals(futureMonth);
                } else if (duration != null && duration > 1) {
                    // Határozott idejű: incStart-tól incStart + (duration - 1) hónapig
                    YearMonth incEnd = incStart.plusMonths(duration - 1);
                    isActiveInMonth = !futureMonth.isBefore(incStart) && !futureMonth.isAfter(incEnd);
                } else {
                    // Állandó / visszatérő határozatlan ideig
                    isActiveInMonth = !futureMonth.isBefore(incStart);
                }

                if (isActiveInMonth && inc.getAmount() != null) {
                    futureMonthIncome = futureMonthIncome.add(inc.getAmount());
                }
            }

            // Ha nem volt kifejezetten definiált bevétel a jövőre, a bázis bevételt használjuk
            if (futureMonthIncome.compareTo(BigDecimal.ZERO) == 0 && cashFlow.getMonthlyIncome().compareTo(BigDecimal.ZERO) > 0) {
                futureMonthIncome = cashFlow.getMonthlyIncome();
            }

            BigDecimal monthNetSavings = futureMonthIncome.subtract(monthlyExpenses);
            runningCalculatedBalance = runningCalculatedBalance.add(monthNetSavings);
            if (runningCalculatedBalance.compareTo(BigDecimal.ZERO) < 0) {
                runningCalculatedBalance = BigDecimal.ZERO;
            }

            // Jövőben a valós egyenleg szigorúan NULL!
            monthlyPoints.add(new BalancePointDto(label, futureMonth.atDay(1), null, runningCalculatedBalance));
        }

        trajectory.setMonthlyPoints(monthlyPoints);
        return trajectory;
    }
}
