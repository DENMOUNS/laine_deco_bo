package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.PromotionResponse;
import cm.dolers.laine_deco.application.dto.CreatePromotionRequest;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PromotionEntity;

/**
 * Mapper pour Promotion
 */
public interface PromotionMapper {
    PromotionResponse toResponse(PromotionEntity promotion);
    PromotionEntity toEntity(CreatePromotionRequest request);
}
