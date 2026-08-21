package hu.haztartas.config;

import hu.haztartas.entity.Category;
import hu.haztartas.entity.Expense;
import hu.haztartas.entity.Income;
import hu.haztartas.entity.SavingsGoal;
import hu.haztartas.entity.enums.CategoryType;
import hu.haztartas.entity.enums.Priority;
import hu.haztartas.entity.enums.RecurrenceFrequency;
import hu.haztartas.repository.CategoryRepository;
import hu.haztartas.repository.ExpenseRepository;
import hu.haztartas.repository.IncomeRepository;
import hu.haztartas.repository.SavingsGoalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public DataInitializer(
            CategoryRepository categoryRepository,
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            SavingsGoalRepository savingsGoalRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            initDefaultData();
        }
    }

    private void initDefaultData() {
        // 1. BEVÉTELI KATEGÓRIÁK
        Category catJob = categoryRepository.save(new Category("Diákmunka", CategoryType.INCOME, false, "briefcase", "#10b981", BigDecimal.ZERO, Priority.NEEDS));
        Category catScholarship = categoryRepository.save(new Category("Egyetemi Ösztöndíj", CategoryType.INCOME, true, "book", "#3b82f6", BigDecimal.ZERO, Priority.NEEDS));
        Category catParents = categoryRepository.save(new Category("Családi támogatás / Zsebpénz", CategoryType.INCOME, true, "heart", "#06b6d4", BigDecimal.ZERO, Priority.NEEDS));
        categoryRepository.save(new Category("Egyéb Bevétel", CategoryType.INCOME, false, "plus-circle", "#8b5cf6", BigDecimal.ZERO, Priority.WANTS));

        // 2. FIX KIADÁSI KATEGÓRIÁK
        Category catDorm = categoryRepository.save(new Category("Kollégium / Albérlet & Rezsi", CategoryType.EXPENSE, true, "home", "#ef4444", BigDecimal.valueOf(50000), Priority.NEEDS));
        Category catPass = categoryRepository.save(new Category("Diákbérlet / Utazás (BKK, Vonat)", CategoryType.EXPENSE, true, "train", "#f97316", BigDecimal.valueOf(10000), Priority.NEEDS));
        Category catNet = categoryRepository.save(new Category("Mobil & Internet előfizetés", CategoryType.EXPENSE, true, "wifi", "#6366f1", BigDecimal.valueOf(6000), Priority.NEEDS));
        Category catStreaming = categoryRepository.save(new Category("Streaming (Spotify, Netflix)", CategoryType.EXPENSE, true, "tv", "#06b6d4", BigDecimal.valueOf(4000), Priority.WANTS));

        // 3. VÁLTOZÓ KIADÁSI KATEGÓRIÁK
        Category catFood = categoryRepository.save(new Category("Élelmiszer, Menza & Bevásárlás", CategoryType.EXPENSE, false, "shopping-cart", "#f59e0b", BigDecimal.valueOf(55000), Priority.NEEDS));
        Category catFun = categoryRepository.save(new Category("Szórakozás, Kávé, Buli", CategoryType.EXPENSE, false, "coffee", "#a855f7", BigDecimal.valueOf(25000), Priority.WANTS));
        Category catStudy = categoryRepository.save(new Category("Egyetemi jegyzetek & Eszközök", CategoryType.EXPENSE, false, "edit", "#ec4899", BigDecimal.valueOf(10000), Priority.NEEDS));

        // 4. MINTA BEVÉTELEK
        incomeRepository.save(new Income("Diákmunka fizetés", BigDecimal.valueOf(140000), RecurrenceFrequency.MONTHLY, catJob, LocalDate.now().withDayOfMonth(10), "Heti 20 óra munka", true));
        incomeRepository.save(new Income("Tanulmányi Ösztöndíj", BigDecimal.valueOf(35000), RecurrenceFrequency.MONTHLY, catScholarship, LocalDate.now().withDayOfMonth(5), "Egyetemi ösztöndíj", true));
        incomeRepository.save(new Income("Szülői támogatás", BigDecimal.valueOf(45000), RecurrenceFrequency.MONTHLY, catParents, LocalDate.now().withDayOfMonth(1), "Havi támogatás", true));

        // 5. MINTA KIADÁSOK
        expenseRepository.save(new Expense("Kollégiumi havidíj", BigDecimal.valueOf(35000), true, catDorm, LocalDate.now().withDayOfMonth(10), 10, Priority.NEEDS, "Koli szoba", true));
        expenseRepository.save(new Expense("BKK diákbérlet", BigDecimal.valueOf(3450), true, catPass, LocalDate.now().withDayOfMonth(5), 5, Priority.NEEDS, "Havi bérlet", true));
        expenseRepository.save(new Expense("Mobilnet előfizetés", BigDecimal.valueOf(4990), true, catNet, LocalDate.now().withDayOfMonth(15), 15, Priority.NEEDS, "Korlátlan net", true));
        expenseRepository.save(new Expense("Spotify Diák", BigDecimal.valueOf(1590), true, catStreaming, LocalDate.now().withDayOfMonth(8), 8, Priority.WANTS, "Diák csomag", true));
        expenseRepository.save(new Expense("Menza & Heti kaja", BigDecimal.valueOf(42000), false, catFood, LocalDate.now().withDayOfMonth(3), 1, Priority.NEEDS, "Étkezés", true));
        expenseRepository.save(new Expense("Kávé & Egyetemi büfé", BigDecimal.valueOf(11500), false, catFun, LocalDate.now().withDayOfMonth(12), 1, Priority.WANTS, "Kávézás", true));

        // 6. MINTA MEGTAKARÍTÁSI CÉLOK
        savingsGoalRepository.save(new SavingsGoal("Új Tanulmányi Laptop", BigDecimal.valueOf(250000), BigDecimal.valueOf(95000), LocalDate.now().plusMonths(6), "#10b981", "laptop", "Egyetemi gép"));
        savingsGoalRepository.save(new SavingsGoal("Nyári Fesztiválbérlet / Utazás", BigDecimal.valueOf(120000), BigDecimal.valueOf(40000), LocalDate.now().plusMonths(4), "#3b82f6", "music", "Nyári pihenés"));
    }
}
