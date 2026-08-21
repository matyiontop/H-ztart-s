package hu.haztartas.repository;

import hu.haztartas.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {
    List<BalanceHistory> findAllByOrderByRecordDateAsc();
    List<BalanceHistory> findByRecordDateBetweenOrderByRecordDateAsc(LocalDate start, LocalDate end);
}
