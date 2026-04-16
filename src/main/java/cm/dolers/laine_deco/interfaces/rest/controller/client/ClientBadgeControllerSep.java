package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.UserBadgeResponse;
import cm.dolers.laine_deco.application.usecase.BadgeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Client Controller pour Badges
 * Responsabilité: Afficher les badges de l'utilisateur connecté
 */
@RestController
@RequestMapping("/api/client/badges")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientBadgeController {
    private final BadgeService badgeService;

    @GetMapping("/me")
    public ResponseEntity<List<UserBadgeResponse>> getMyBadges(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/badges/me - User: {}", userId);
        var response = badgeService.getUserBadges(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/recent")
    public ResponseEntity<List<UserBadgeResponse>> getRecentBadges(@RequestParam(defaultValue = "5") int limit,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/badges/me/recent - User: {}", userId);
        var response = badgeService.getRecentBadges(userId, limit);
        return ResponseEntity.ok(response);
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        // TODO: Implémenter correctement avec JWT
        return 1L;
    }
}
