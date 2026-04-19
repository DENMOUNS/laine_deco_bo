package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ReviewMapper;
import cm.dolers.laine_deco.application.usecase.ReviewService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ReviewException;
import cm.dolers.laine_deco.domain.model.ReviewStatus;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ReviewEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewJpaRepository reviewRepository;
    private final ProductJpaRepository ProductJpaRepository;
    private final UserJpaRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {
        log.info("Creating review for product: {} by user: {}", request.productId(), userId);

        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ReviewException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        var product = ProductJpaRepository.findById(request.productId())
            .orElseThrow(() -> new ReviewException(ErrorCode.PRODUCT_NOT_FOUND, "Product ID: " + request.productId()));

        if (request.rating() < 1 || request.rating() > 5) {
            throw new ReviewException(ErrorCode.REVIEW_INVALID_RATING);
        }

        try {
            var review = new ReviewEntity();
            review.setProduct(product);
            review.setUser(user);
            review.setRating(request.rating());
            review.setTitle(request.title());
            review.setComment(request.comment());
            review.setStatus(ReviewStatus.PENDING);
            review.setHelpfulCount(0);

            var saved = reviewRepository.save(review);
            log.info("Review created: {}", saved.getId());
            return reviewMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating review", ex);
            throw new ReviewException(ErrorCode.OPERATION_FAILED, "Error creating review", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long reviewId) {
        var review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND, "ID: " + reviewId));
        return reviewMapper.toResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getProductReviews(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable)
            .map(reviewMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getUserReviews(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
            .map(reviewMapper::toResponse);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ReviewException(ErrorCode.REVIEW_NOT_FOUND, "ID: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
        log.info("Review deleted: {}", reviewId);
    }

    @Override
    @Transactional
    public void deleteUserReview(Long reviewId, Long userId) {
        var review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND, "ID: " + reviewId));
            
        if (!review.getUser().getId().equals(userId)) {
            throw new ReviewException(ErrorCode.REVIEW_UNAUTHORIZED, "You can only delete your own reviews");
        }
        
        if (review.getCreatedAt() != null && review.getCreatedAt().plusSeconds(5 * 60).isBefore(java.time.Instant.now())) {
            throw new ReviewException(ErrorCode.REVIEW_UNAUTHORIZED, "You cannot delete a review 5 minutes after its creation");
        }
        
        reviewRepository.deleteById(reviewId);
        log.info("Review {} deleted by user {}", reviewId, userId);
    }

    @Override
    @Transactional
    public void approveReview(Long reviewId) {
        var review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND, "ID: " + reviewId));
        review.setStatus(ReviewStatus.APPROVED);
        reviewRepository.save(review);
        log.info("Review approved: {}", reviewId);
    }

    @Override
    @Transactional
    public void rejectReview(Long reviewId) {
        var review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND, "ID: " + reviewId));
        review.setStatus(ReviewStatus.REJECTED);
        reviewRepository.save(review);
        log.info("Review rejected: {}", reviewId);
    }
}

