package hu.haztartas.repository;

import hu.haztartas.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findAllByOrderByReceivedDateDesc();
    
    List<Income> findByReceivedDateBetweenOrderByReceivedDateDesc(LocalDate startDate, LocalDate endDate);
    
    List<Income> findByIsRecurring(boolean isRecurring);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i")
    BigDecimal sumTotalIncomes();

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.receivedDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.isRecurring = true")
    BigDecimal sumMonthlyRecurringAmount();
}
