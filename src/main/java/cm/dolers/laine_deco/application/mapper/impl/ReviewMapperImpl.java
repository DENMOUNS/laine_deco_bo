package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ReviewMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ReviewEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponse toResponse(ReviewEntity review) {
        return new ReviewResponse(
            review.getId(),
            review.getProduct().getId(),
            review.getUser().getId(),
            review.getUser().getName(),
            review.getRating(),
            review.getComment() != null ? review.getComment().substring(0, Math.min(50, review.getComment().length())) : "No title",
            review.getComment(),
            review.getStatus() != null ? review.getStatus().toString() : "PENDING",
            0,
            review.getCreatedAt()
        );
    }
}
