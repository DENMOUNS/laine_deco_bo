package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Client Controller pour laisser des avis
 */
@RestController
@RequestMapping("/api/client/reviews")
@RequiredArgsConstructor

@PreAuthorize("hasRole('CLIENT')")
public class ClientReviewController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientReviewController.class);
    private final ReviewService reviewService;

    /**
     * Créer un avis produit
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromToken(httpRequest);
        log.info("POST /api/client/reviews - User: {}, Product: {}", userId, request.productId());
        var response = reviewService.createReview(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupérer mes avis
     */
    @GetMapping("/me")
    public ResponseEntity<Page<ReviewResponse>> getMyReviews(
            Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/reviews/me - User: {}", userId);
        var response = reviewService.getUserReviews(userId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer mon avis
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        log.info("DELETE /api/client/reviews/{}", reviewId);
        Long userId = extractUserIdFromToken(null);
        reviewService.deleteUserReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser user) {
            return user.getId();
        }
        return 1L; // Fallback local
    }
}


