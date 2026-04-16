package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

import cm.dolers.laine_deco.application.dto.BadgeResponse;
import cm.dolers.laine_deco.application.usecase.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public Controller pour Badges
 * Responsabilité: Afficher les badges disponibles
 */
@RestController
@RequestMapping("/api/public/badges")
@RequiredArgsConstructor
@Slf4j
public class PublicBadgeController {
    private final BadgeService badgeService;

    @GetMapping("/{id}")
    public ResponseEntity<BadgeResponse> getBadge(@PathVariable Long id) {
        log.info("GET /api/public/badges/{}", id);
        var response = badgeService.getBadgeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BadgeResponse>> getAllBadges(Pageable pageable) {
        log.info("GET /api/public/badges");
        var response = badgeService.getAllBadges(pageable);
        return ResponseEntity.ok(response);
    }
}
