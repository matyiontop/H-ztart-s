package hu.haztartas.controller;

import hu.haztartas.dto.SavingsGoalDto;
import hu.haztartas.service.SavingsGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@Tag(name = "Megtakarítási Célok", description = "Célok, vésztartalék és haladás követése")
@CrossOrigin(origins = "*")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @GetMapping
    @Operation(summary = "Összes cél listázása")
    public ResponseEntity<List<SavingsGoalDto>> getAllGoals() {
        return ResponseEntity.ok(savingsGoalService.getAllGoals());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Cél lekérése azonosító alapján")
    public ResponseEntity<SavingsGoalDto> getGoalById(@PathVariable Long id) {
        return ResponseEntity.ok(savingsGoalService.getGoalById(id));
    }

    @PostMapping
    @Operation(summary = "Új cél létrehozása")
    public ResponseEntity<SavingsGoalDto> createGoal(@Valid @RequestBody SavingsGoalDto dto) {
        return new ResponseEntity<>(savingsGoalService.createGoal(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cél módosítása")
    public ResponseEntity<SavingsGoalDto> updateGoal(@PathVariable Long id, @Valid @RequestBody SavingsGoalDto dto) {
        return ResponseEntity.ok(savingsGoalService.updateGoal(id, dto));
    }

    @PostMapping("/{id}/deposit")
    @Operation(summary = "Összeg befizetése / hozzáadása a célhoz")
    public ResponseEntity<SavingsGoalDto> addDeposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(savingsGoalService.addDeposit(id, amount));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cél törlése")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        savingsGoalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
