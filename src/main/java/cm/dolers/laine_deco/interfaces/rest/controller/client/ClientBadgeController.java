package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.usecase.BadgeService;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/badges")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientBadgeController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientBadgeController.class);
    private final BadgeService badgeService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyBadges() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/badges/me - User: {}", userId);
        var response = badgeService.getUserBadges(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/recent")
    public ResponseEntity<?> getRecentBadges(@RequestParam(defaultValue = "5") int limit) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/badges/me/recent - User: {}", userId);
        var response = badgeService.getRecentBadges(userId, limit);
        return ResponseEntity.ok(response);
    }
}


