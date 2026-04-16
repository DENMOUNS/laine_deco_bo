package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
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
 * Client Controller pour laisser des avis
 */
@RestController
@RequestMapping("/api/client/reviews")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientReviewController {
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
        // TODO: Vérifier que c'est l'avis de l'utilisateur connecté
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        // TODO: Implémenter correctement
        return 1L;
    }
}
