package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductPackEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductPackProductEntity;
import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ProductPackMapper;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductPackMapperImpl implements ProductPackMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public ProductPackResponse toResponse(ProductPackEntity entity) {
        ProductPackResponse response = new ProductPackResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setSalePrice(entity.getSalePrice());
        response.setDiscountedPrice(entity.getDiscountedPrice());
        response.setPromotionalDiscount(entity.getPromotionalDiscount());
        response.setImage(entity.getImage());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt().toString());
        response.setUpdatedAt(entity.getUpdatedAt().toString());
        response.setTotalProductCount(entity.getTotalProductCount());
        response.setUniqueProductCount(entity.getUniqueProductCount());
        response.setItems(entity.getPackProducts().stream()
            .map(this::toItemDto)
            .collect(Collectors.toList()));
        return response;
    }

    @Override
    public List<ProductPackResponse> toResponseList(List<ProductPackEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ProductPackItemDto toItemDto(ProductPackProductEntity entity) {
        ProductPackItemDto dto = new ProductPackItemDto();
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setProductPrice(entity.getProduct().getSalePrice().longValue());
        dto.setQuantity(entity.getQuantity());
        dto.setProductImage(null);
        dto.setProductCategory(entity.getProduct().getCategory().getName());
        dto.setProductBrand(entity.getProduct().getBrand());
        return dto;
    }
}
