package hu.haztartas.service;

import hu.haztartas.dto.AccountBalanceDto;
import hu.haztartas.dto.CashFlowSummaryDto;
import hu.haztartas.entity.AccountBalance;
import hu.haztartas.repository.AccountBalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class AccountBalanceService {

    private final AccountBalanceRepository accountBalanceRepository;
    private final CalculationService calculationService;

    public AccountBalanceService(AccountBalanceRepository accountBalanceRepository, CalculationService calculationService) {
        this.accountBalanceRepository = accountBalanceRepository;
        this.calculationService = calculationService;
    }

    public AccountBalanceDto getBalanceDto() {
        AccountBalance entity = accountBalanceRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    AccountBalance def = new AccountBalance(BigDecimal.valueOf(150000), BigDecimal.valueOf(140000), BigDecimal.valueOf(10000), "Kezdő egyenleg");
                    return accountBalanceRepository.save(def);
                });

        CashFlowSummaryDto cashFlow = calculationService.calculateCashFlow();
        BigDecimal projected = entity.getBalance().add(cashFlow.getNetMonthlySavings());

        return new AccountBalanceDto(
                entity.getId(),
                entity.getBalance(),
                entity.getBankAmount(),
                entity.getCashAmount(),
                entity.getUpdatedAt() != null ? entity.getUpdatedAt() : LocalDateTime.now(),
                entity.getNote(),
                projected
        );
    }

    public AccountBalanceDto updateBalance(AccountBalanceDto dto) {
        AccountBalance entity = accountBalanceRepository.findFirstByOrderByIdAsc()
                .orElseGet(AccountBalance::new);

        BigDecimal balance = dto.getBalance() != null ? dto.getBalance() : BigDecimal.ZERO;
        BigDecimal bank = dto.getBankAmount() != null ? dto.getBankAmount() : balance;
        BigDecimal cash = dto.getCashAmount() != null ? dto.getCashAmount() : BigDecimal.ZERO;

        entity.setBalance(balance);
        entity.setBankAmount(bank);
        entity.setCashAmount(cash);
        entity.setNote(dto.getNote());
        entity.setUpdatedAt(LocalDateTime.now());

        AccountBalance saved = accountBalanceRepository.save(entity);

        CashFlowSummaryDto cashFlow = calculationService.calculateCashFlow();
        BigDecimal projected = saved.getBalance().add(cashFlow.getNetMonthlySavings());

        return new AccountBalanceDto(
                saved.getId(),
                saved.getBalance(),
                saved.getBankAmount(),
                saved.getCashAmount(),
                saved.getUpdatedAt(),
                saved.getNote(),
                projected
        );
    }
}
