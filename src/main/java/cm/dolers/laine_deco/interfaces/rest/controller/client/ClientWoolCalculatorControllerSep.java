package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.WoolCalculatorService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Client Controller pour Wool Calculator
 * Responsabilité: Calculer les quantités de laine et sauvegarder les calculs
 */
@RestController
@RequestMapping("/api/client/wool-calculator")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientWoolCalculatorController {
    private final WoolCalculatorService calculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<WoolCalculatorResponse> calculateWool(@Valid @RequestBody CreateWoolCalculatorRequest request) {
        log.info("POST /api/client/wool-calculator/calculate");
        var response = calculatorService.calculateWool(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<WoolCalculatorResponse> saveCalculation(@Valid @RequestBody CreateWoolCalculatorRequest request) {
        log.info("POST /api/client/wool-calculator/save");
        var response = calculatorService.saveCalculation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WoolCalculatorResponse> getCalculation(@PathVariable Long id) {
        log.info("GET /api/client/wool-calculator/{}", id);
        var response = calculatorService.getCalculationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<WoolCalculatorResponse>> getMyCalculations(Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/wool-calculator/me - User: {}", userId);
        var response = calculatorService.getUserCalculations(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalculation(@PathVariable Long id) {
        log.info("DELETE /api/client/wool-calculator/{}", id);
        calculatorService.deleteCalculation(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        // TODO: Implémenter correctement avec JWT
        return 1L;
    }
}
