package hu.haztartas.controller;

import hu.haztartas.dto.*;
import hu.haztartas.service.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculations")
@Tag(name = "Kalkulációk & Szimulációk", description = "Háztartási mérleg, 50/30/20, Vésztartalék és Forgatókönyv-szimulátor")
@CrossOrigin(origins = "*")
public class CalculationController {

    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping("/cashflow")
    @Operation(summary = "Havi és éves Cash Flow (bevételek, fix/változó kiadások, megtakarítás) kalkuláció")
    public ResponseEntity<CashFlowSummaryDto> getCashFlow() {
        return ResponseEntity.ok(calculationService.calculateCashFlow());
    }

    @GetMapping("/50-30-20")
    @Operation(summary = "50/30/20 szabály elemzés (Szükségletek, Vágyak, Megtakarítás)")
    public ResponseEntity<FiftyThirtyTwentyDto> get503020Analysis() {
        return ResponseEntity.ok(calculationService.calculate503020());
    }

    @GetMapping("/emergency-fund")
    @Operation(summary = "Vésztartalék (3, 6, 12 havi biztonsági alap) kalkuláció")
    public ResponseEntity<EmergencyFundDto> getEmergencyFund() {
        return ResponseEntity.ok(calculationService.calculateEmergencyFund());
    }

    @PostMapping("/simulate")
    @Operation(summary = "'Mi lenne, ha...?' interaktív forgatókönyv szimuláció")
    public ResponseEntity<SimulationResultDto> runSimulation(@RequestBody SimulationRequestDto request) {
        return ResponseEntity.ok(calculationService.runSimulation(request));
    }
}
