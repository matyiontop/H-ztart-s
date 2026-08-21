package hu.haztartas.repository;

import hu.haztartas.entity.Expense;
import hu.haztartas.entity.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByOrderByExpenseDateDesc();

    List<Expense> findByExpenseDateBetweenOrderByExpenseDateDesc(LocalDate startDate, LocalDate endDate);

    List<Expense> findByIsFixed(boolean isFixed);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    BigDecimal sumTotalExpenses();

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.isFixed = true")
    BigDecimal sumTotalFixedExpenses();

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.isFixed = false")
    BigDecimal sumTotalVariableExpenses();

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.isFixed = false AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal sumVariableExpensesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.priority = :priority AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByPriorityAndDate(@Param("priority") Priority priority, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(e.category.id, 0) AS categoryId, " +
           "COALESCE(e.category.name, 'Egyéb kiadás') AS categoryName, " +
           "COALESCE(e.category.color, '#6366f1') AS color, " +
           "COALESCE(e.category.icon, 'shopping-bag') AS icon, " +
           "SUM(e.amount) AS totalAmount " +
           "FROM Expense e " +
           "GROUP BY e.category.id, e.category.name, e.category.color, e.category.icon")
    List<Object[]> findCategoryExpensesTotal();
}
