package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.KnittingPatternService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/knitting-patterns")
@RequiredArgsConstructor

@PreAuthorize("hasRole('ADMIN')")
public class AdminKnittingPatternController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminKnittingPatternController.class);
    private final KnittingPatternService patternService;

    @PostMapping
    public ResponseEntity<KnittingPatternResponse> createPattern(
            @Valid @RequestBody CreateKnittingPatternRequest request) {
        log.info("POST /api/admin/knitting-patterns - Creating: {}", request.name());
        var response = patternService.createPattern(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnittingPatternResponse> getPattern(@PathVariable Long id) {
        log.info("GET /api/admin/knitting-patterns/{}", id);
        var response = patternService.getPatternById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<KnittingPatternResponse>> getAllPatterns(Pageable pageable) {
        log.info("GET /api/admin/knitting-patterns");
        var response = patternService.getAllPatterns(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnittingPatternResponse> updatePattern(@PathVariable Long id,
            @Valid @RequestBody CreateKnittingPatternRequest request) {
        log.info("PUT /api/admin/knitting-patterns/{}", id);
        var response = patternService.updatePattern(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePattern(@PathVariable Long id) {
        log.info("DELETE /api/admin/knitting-patterns/{}", id);
        patternService.deletePattern(id);
        return ResponseEntity.noContent().build();
    }
}
