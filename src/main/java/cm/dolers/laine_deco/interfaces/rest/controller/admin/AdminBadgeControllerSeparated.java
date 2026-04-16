package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.BadgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour Badges/Achievements
 * Responsabilité: Créer/gérer les badges et les attribuer aux utilisateurs
 */
@RestController
@RequestMapping("/api/admin/badges")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminBadgeController {
    private final BadgeService badgeService;

    @PostMapping
    public ResponseEntity<BadgeResponse> createBadge(@Valid @RequestBody CreateBadgeRequest request) {
        log.info("POST /api/admin/badges - Creating: {}", request.name());
        var response = badgeService.createBadge(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeResponse> getBadge(@PathVariable Long id) {
        log.info("GET /api/admin/badges/{}", id);
        var response = badgeService.getBadgeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BadgeResponse>> getAllBadges(Pageable pageable) {
        log.info("GET /api/admin/badges");
        var response = badgeService.getAllBadges(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{badgeId}/users/{userId}")
    public ResponseEntity<Void> awardBadge(@PathVariable Long badgeId, @PathVariable Long userId) {
        log.info("POST /api/admin/badges/{}/users/{}", badgeId, userId);
        badgeService.awardBadgeToUser(userId, badgeId);
        return ResponseEntity.ok().build();
    }
}
