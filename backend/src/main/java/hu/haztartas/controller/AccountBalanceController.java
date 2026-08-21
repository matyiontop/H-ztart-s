package hu.haztartas.controller;

import hu.haztartas.dto.AccountBalanceDto;
import hu.haztartas.service.AccountBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Account Balance", description = "Aktuális vagyoni egyenleg és számlaállás kezelése")
@CrossOrigin(origins = "*")
public class AccountBalanceController {

    private final AccountBalanceService accountBalanceService;

    public AccountBalanceController(AccountBalanceService accountBalanceService) {
        this.accountBalanceService = accountBalanceService;
    }

    @GetMapping("/balance")
    @Operation(summary = "Jelenlegi vagyoni egyenleg lekérdezése")
    public ResponseEntity<AccountBalanceDto> getBalance() {
        return ResponseEntity.ok(accountBalanceService.getBalanceDto());
    }

    @PutMapping("/balance")
    @Operation(summary = "Jelenlegi vagyoni egyenleg frissítése és mentése az adatbázisba")
    public ResponseEntity<AccountBalanceDto> updateBalance(@RequestBody AccountBalanceDto dto) {
        return ResponseEntity.ok(accountBalanceService.updateBalance(dto));
    }
}
