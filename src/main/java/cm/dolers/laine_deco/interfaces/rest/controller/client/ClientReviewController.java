package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.ReviewService;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientReviewController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientReviewController.class);
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("POST /api/client/reviews - User: {}, Product: {}", userId, request.productId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(userId, request));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<ReviewResponse>> getMyReviews(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("GET /api/client/reviews/me - User: {}", userId);
        return ResponseEntity.ok(reviewService.getUserReviews(userId, pageable));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("DELETE /api/client/reviews/{}", reviewId);
        reviewService.deleteUserReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }
}
