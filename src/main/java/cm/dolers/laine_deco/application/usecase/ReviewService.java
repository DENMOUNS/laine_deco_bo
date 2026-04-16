package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface Service pour Reviews
 * Gestion des avis produits
 */
public interface ReviewService {
    ReviewResponse createReview(Long userId, CreateReviewRequest request);
    
    ReviewResponse getReviewById(Long reviewId);
    
    Page<ReviewResponse> getProductReviews(Long productId, Pageable pageable);
    
    Page<ReviewResponse> getUserReviews(Long userId, Pageable pageable);
    
    void deleteReview(Long reviewId);
    
    void approveReview(Long reviewId);
    
    void rejectReview(Long reviewId);
}
