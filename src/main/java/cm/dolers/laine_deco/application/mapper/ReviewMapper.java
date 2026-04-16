package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ReviewEntity;

/**
 * Interface Mapper pour Review
 */
public interface ReviewMapper {
    ReviewResponse toResponse(ReviewEntity review);
}
