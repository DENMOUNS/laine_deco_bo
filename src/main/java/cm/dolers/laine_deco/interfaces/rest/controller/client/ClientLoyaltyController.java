package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.UserLoyaltyResponse;
import cm.dolers.laine_deco.application.dto.UserBadgeResponse;
import cm.dolers.laine_deco.application.dto.LoyaltyRedemptionResponse;
import cm.dolers.laine_deco.application.dto.RedeemRewardRequest;
import cm.dolers.laine_deco.application.usecase.LoyaltyService;
import cm.dolers.laine_deco.application.usecase.BadgeService;
import cm.dolers.laine_deco.infrastructure.config.PaginationConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Client Controller pour consulter loyauté, badges et redémer des récompenses
 */
@RestController
@RequestMapping("/api/client/loyalty")
@RequiredArgsConstructor

@PreAuthorize("hasRole('CLIENT')")
public class ClientLoyaltyController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientLoyaltyController.class);
    private final LoyaltyService loyaltyService;
    private final BadgeService badgeService;

    /**
     * Récupère le profil de loyauté de l'utilisateur
     */
    @GetMapping("/profile")
    public ResponseEntity<UserLoyaltyResponse> getLoyaltyProfile(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/loyalty/profile - User: {}", userId);
        var response = loyaltyService.getUserLoyalty(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère les badges de l'utilisateur
     */
    @GetMapping("/badges")
    public ResponseEntity<Page<UserBadgeResponse>> getBadges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);

        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        log.info("GET /api/client/loyalty/badges - User: {}, page: {}, size: {}", userId, page, normalizedSize);

        List<UserBadgeResponse> badges = badgeService.getUserBadges(userId);

        // Appliquer la pagination manuellement
        int start = page * normalizedSize;
        int end = Math.min(start + normalizedSize, badges.size());
        List<UserBadgeResponse> pageContent = badges.subList(start, end);

        Page<UserBadgeResponse> pageResult = new PageImpl<>(
                pageContent,
                org.springframework.data.domain.PageRequest.of(page, normalizedSize),
                badges.size());

        return ResponseEntity.ok(pageResult);
    }

    /**
     * Récupère les badges avec pagination
     */
    @GetMapping("/badges/paginated")
    public ResponseEntity<Page<UserBadgeResponse>> getBadgesPaginated(
            Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/loyalty/badges/paginated - User: {}", userId);
        var badges = badgeService.getUserBadgesPaginated(userId, pageable);
        return ResponseEntity.ok(badges);
    }

    /**
     * Redémer une récompense avec les points de loyauté
     */
    @PostMapping("/redeem")
    public ResponseEntity<LoyaltyRedemptionResponse> redeemReward(
            @Valid @RequestBody RedeemRewardRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromToken(httpRequest);
        log.info("POST /api/client/loyalty/redeem - User: {}, type: {}", userId, request.rewardType());
        var response = loyaltyService.redeemReward(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupère l'historique des rédemptions
     */
    @GetMapping("/redemptions")
    public ResponseEntity<Page<LoyaltyRedemptionResponse>> getRedemptions(
            Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/loyalty/redemptions - User: {}", userId);
        var redemptions = loyaltyService.getUserRedemptions(userId, pageable);
        return ResponseEntity.ok(redemptions);
    }

    /**
     * Récupère les rédemptions en attente
     */
    @GetMapping("/redemptions/pending")
    public ResponseEntity<Page<LoyaltyRedemptionResponse>> getPendingRedemptions(
            Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/loyalty/redemptions/pending - User: {}", userId);
        var redemptions = loyaltyService.getPendingRedemptions(userId, pageable);
        return ResponseEntity.ok(redemptions);
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser user) {
            return user.getId();
        }
        return 1L; // Fallback local
    }
}

