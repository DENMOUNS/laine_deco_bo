package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.KnittingPatternResponse;
import cm.dolers.laine_deco.application.usecase.KnittingPatternService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public Controller pour Knitting Patterns
 * Responsabilité: Afficher les modèles de tricot disponibles
 */
@RestController
@RequestMapping("/api/public/knitting-patterns")
@RequiredArgsConstructor

public class PublicKnittingPatternController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory
            .getLogger(PublicKnittingPatternController.class);
    private final KnittingPatternService patternService;

    @GetMapping("/{id}")
    public ResponseEntity<KnittingPatternResponse> getPattern(@PathVariable Long id) {
        log.info("GET /api/public/knitting-patterns/{}", id);
        var response = patternService.getPatternById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<KnittingPatternResponse>> getPublishedPatterns(Pageable pageable) {
        log.info("GET /api/public/knitting-patterns");
        var response = patternService.getPublishedPatterns(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<KnittingPatternResponse>> searchPatterns(@RequestParam String query, Pageable pageable) {
        log.info("GET /api/public/knitting-patterns/search - Query: {}", query);
        var response = patternService.searchPatterns(query, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/level/{skillLevel}")
    public ResponseEntity<Page<KnittingPatternResponse>> getPatternsBySkillLevel(@PathVariable String skillLevel,
            Pageable pageable) {
        log.info("GET /api/public/knitting-patterns/level/{}", skillLevel);
        var response = patternService.getPatternsBySkillLevel(skillLevel, pageable);
        return ResponseEntity.ok(response);
    }
}
