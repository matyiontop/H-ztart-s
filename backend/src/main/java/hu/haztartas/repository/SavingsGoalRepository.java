package hu.haztartas.repository;

import hu.haztartas.entity.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    List<SavingsGoal> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COALESCE(SUM(g.currentAmount), 0) FROM SavingsGoal g")
    BigDecimal sumTotalSavedAmount();

    @Query("SELECT COALESCE(SUM(g.targetAmount), 0) FROM SavingsGoal g")
    BigDecimal sumTotalTargetAmount();
}
