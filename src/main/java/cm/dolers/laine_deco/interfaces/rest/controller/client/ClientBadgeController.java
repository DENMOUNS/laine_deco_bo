package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.usecase.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/client/badges")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientBadgeController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientBadgeController.class);
    private final BadgeService badgeService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyBadges(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/badges/me - User: {}", userId);
        var response = badgeService.getUserBadges(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/recent")
    public ResponseEntity<?> getRecentBadges(@RequestParam(defaultValue = "5") int limit,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/badges/me/recent - User: {}", userId);
        var response = badgeService.getRecentBadges(userId, limit);
        return ResponseEntity.ok(response);
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser user) {
            return user.getId();
        }
        return 1L; // Fallback local
    }
}


