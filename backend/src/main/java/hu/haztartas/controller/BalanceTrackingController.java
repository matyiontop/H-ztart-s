package hu.haztartas.controller;

import hu.haztartas.dto.BalanceTrajectoryDto;
import hu.haztartas.service.BalanceTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Balance Tracking", description = "Valós vs. Kalkulált egyenleg idővonala és trendje")
@CrossOrigin(origins = "*")
public class BalanceTrackingController {

    private final BalanceTrackingService balanceTrackingService;

    public BalanceTrackingController(BalanceTrackingService balanceTrackingService) {
        this.balanceTrackingService = balanceTrackingService;
    }

    @GetMapping("/trajectory")
    @Operation(summary = "Kalkulált és valós egyenleg idővonalának lekérdezése")
    public ResponseEntity<BalanceTrajectoryDto> getTrajectory() {
        return ResponseEntity.ok(balanceTrackingService.getTrajectory());
    }
}
