package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.CreatePromotionRequest;
import cm.dolers.laine_deco.application.dto.PromotionResponse;
import cm.dolers.laine_deco.application.mapper.PromotionMapper;
import cm.dolers.laine_deco.domain.model.PromotionType;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PromotionEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.CategoryJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMapperImpl implements PromotionMapper {
    private final CategoryJpaRepository CategoryJpaRepository;
    private final ProductJpaRepository ProductJpaRepository;

    @Override
    public PromotionResponse toResponse(PromotionEntity promotion) {
        return new PromotionResponse(
            promotion.getId(),
            promotion.getName(),
            promotion.getDescription(),
            promotion.getType().getValue(),
            promotion.getStartDate(),
            promotion.getEndDate(),
            promotion.getIsActive(),
            promotion.getDiscountPercentage(),
            promotion.getDiscountAmount(),
            promotion.getCategory() != null ? promotion.getCategory().getId() : null,
            promotion.getCategory() != null ? promotion.getCategory().getName() : null,
            promotion.getBrand(),
            promotion.getProduct() != null ? promotion.getProduct().getId() : null,
            promotion.getProduct() != null ? promotion.getProduct().getName() : null,
            promotion.getTimeRemainingMs(),
            promotion.getTimeRemainingFormatted()
        );
    }

    @Override
    public PromotionEntity toEntity(CreatePromotionRequest request) {
        PromotionEntity promotion = new PromotionEntity();
        promotion.setName(request.name());
        promotion.setDescription(request.description());
        promotion.setType(PromotionType.valueOf(request.type()));
        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        promotion.setDiscountPercentage(request.discountPercentage());
        promotion.setDiscountAmount(request.discountAmount());
        promotion.setBrand(request.brand());
        promotion.setIsActive(true);

        // RÃ©cupÃ©rer la catÃ©gorie si fournie
        if (request.categoryId() != null) {
            var category = CategoryJpaRepository.findById(request.categoryId());
            category.ifPresent(promotion::setCategory);
        }

        // RÃ©cupÃ©rer le produit si fourni
        if (request.productId() != null) {
            var product = ProductJpaRepository.findById(request.productId());
            product.ifPresent(promotion::setProduct);
        }

        return promotion;
    }
}

