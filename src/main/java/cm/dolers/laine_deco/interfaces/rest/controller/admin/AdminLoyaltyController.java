package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.UserLoyaltyResponse;
import cm.dolers.laine_deco.application.dto.UserBadgeResponse;
import cm.dolers.laine_deco.application.dto.LoyaltyRedemptionResponse;
import cm.dolers.laine_deco.application.usecase.LoyaltyService;
import cm.dolers.laine_deco.application.usecase.BadgeService;
import cm.dolers.laine_deco.domain.model.BadgeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Controller pour gérer la loyauté et les badges des utilisateurs
 */
@RestController
@RequestMapping("/api/admin/loyalty")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminLoyaltyController {
    private final LoyaltyService loyaltyService;
    private final BadgeService badgeService;

    /**
     * Récupère le profil de loyauté d'un utilisateur
     */
    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<UserLoyaltyResponse> getUserLoyaltyProfile(@PathVariable Long userId) {
        log.info("GET /api/admin/loyalty/users/{}/profile", userId);
        var response = loyaltyService.getUserLoyalty(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère les badges d'un utilisateur
     */
    @GetMapping("/users/{userId}/badges")
    public ResponseEntity<List<UserBadgeResponse>> getUserBadges(@PathVariable Long userId) {
        log.info("GET /api/admin/loyalty/users/{}/badges", userId);
        var badges = badgeService.getUserBadges(userId);
        return ResponseEntity.ok(badges);
    }

    /**
     * Récupère les badges avec pagination
     */
    @GetMapping("/users/{userId}/badges/paginated")
    public ResponseEntity<Page<UserBadgeResponse>> getUserBadgesPaginated(
            @PathVariable Long userId,
            Pageable pageable) {
        log.info("GET /api/admin/loyalty/users/{}/badges/paginated", userId);
        var badges = badgeService.getUserBadgesPaginated(userId, pageable);
        return ResponseEntity.ok(badges);
    }

    /**
     * Attribue un badge à un utilisateur (manuellement)
     */
    @PostMapping("/users/{userId}/badges/{badgeType}")
    public ResponseEntity<UserBadgeResponse> awardBadge(
            @PathVariable Long userId,
            @PathVariable String badgeType,
            @RequestParam(defaultValue = "Badge manuel attribué par admin") String description) {
        log.info("POST /api/admin/loyalty/users/{}/badges/{}", userId, badgeType);
        try {
            var badge = BadgeType.valueOf(badgeType);
            var response = badgeService.awardBadge(userId, badge, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid badge type: {}", badgeType);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère l'historique des rédemptions d'un utilisateur
     */
    @GetMapping("/users/{userId}/redemptions")
    public ResponseEntity<Page<LoyaltyRedemptionResponse>> getUserRedemptions(
            @PathVariable Long userId,
            Pageable pageable) {
        log.info("GET /api/admin/loyalty/users/{}/redemptions", userId);
        var redemptions = loyaltyService.getUserRedemptions(userId, pageable);
        return ResponseEntity.ok(redemptions);
    }

    /**
     * Récupère les rédemptions en attente d'un utilisateur
     */
    @GetMapping("/users/{userId}/redemptions/pending")
    public ResponseEntity<Page<LoyaltyRedemptionResponse>> getUserPendingRedemptions(
            @PathVariable Long userId,
            Pageable pageable) {
        log.info("GET /api/admin/loyalty/users/{}/redemptions/pending", userId);
        var redemptions = loyaltyService.getPendingRedemptions(userId, pageable);
        return ResponseEntity.ok(redemptions);
    }
}
