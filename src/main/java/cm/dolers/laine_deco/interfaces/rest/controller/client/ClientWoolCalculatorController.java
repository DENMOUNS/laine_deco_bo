package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.WoolCalculatorService;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/client/wool-calculator")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientWoolCalculatorController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientWoolCalculatorController.class);
    private final WoolCalculatorService calculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<WoolCalculatorResponse> calculateWool(@Valid @RequestBody CreateWoolCalculatorRequest request) {
        log.info("POST /api/client/wool-calculator/calculate");
        return ResponseEntity.ok(calculatorService.calculateWool(request));
    }

    @PostMapping("/save")
    public ResponseEntity<WoolCalculatorResponse> saveCalculation(@Valid @RequestBody CreateWoolCalculatorRequest request) {
        log.info("POST /api/client/wool-calculator/save");
        return ResponseEntity.status(HttpStatus.CREATED).body(calculatorService.saveCalculation(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WoolCalculatorResponse> getCalculation(@PathVariable Long id) {
        log.info("GET /api/client/wool-calculator/{}", id);
        return ResponseEntity.ok(calculatorService.getCalculationById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<WoolCalculatorResponse>> getMyCalculations(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/wool-calculator/me - User: {}", userId);
        return ResponseEntity.ok(calculatorService.getUserCalculations(userId, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalculation(@PathVariable Long id) {
        log.info("DELETE /api/client/wool-calculator/{}", id);
        calculatorService.deleteCalculation(id);
        return ResponseEntity.noContent().build();
    }
}
