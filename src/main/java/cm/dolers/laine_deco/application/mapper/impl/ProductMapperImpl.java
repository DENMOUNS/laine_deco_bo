package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ProductMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponse toResponse(ProductEntity product) {
        BigDecimal margin = product.getSalePrice() != null && product.getCostPrice() != null
            ? product.getSalePrice().subtract(product.getCostPrice())
            : BigDecimal.ZERO;
            
        BigDecimal marginPercentage = product.getCostPrice() != null && product.getCostPrice().compareTo(BigDecimal.ZERO) > 0
            ? margin.multiply(new BigDecimal(100)).divide(product.getCostPrice(), 2, java.math.RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        return new ProductResponse(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getDescription(),
            product.getSalePrice(),
            product.getCostPrice(),
            product.getStockQuantity(),
            product.getReorderLevel(),
            margin,
            marginPercentage
        );
    }

    @Override
    public ProductDetailResponse toDetailResponse(ProductEntity product) {
        BigDecimal margin = product.getSalePrice() != null && product.getCostPrice() != null
            ? product.getSalePrice().subtract(product.getCostPrice())
            : BigDecimal.ZERO;
            
        BigDecimal marginPercentage = product.getCostPrice() != null && product.getCostPrice().compareTo(BigDecimal.ZERO) > 0
            ? margin.multiply(new BigDecimal(100)).divide(product.getCostPrice(), 2, java.math.RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        return new ProductDetailResponse(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getDescription(),
            product.getSalePrice(),
            product.getCostPrice(),
            margin,
            marginPercentage,
            product.getStockQuantity(),
            product.getReorderLevel(),
            product.getMaterial(),
            product.getColors(),
            product.getBrand(),
            product.getRating(),
            product.getIsNew(),
            product.getIsSale(),
            product.getIsAvailable(),
            product.getWarranty(),
            product.getIsElectronic(),
            product.getViewsCount(),
            product.getSalesCount(),
            product.getCategory() != null ? product.getCategory().getId() : null,
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }

    @Override
    public ProductEntity createFromRequest(CreateProductRequest request) {
        var product = new ProductEntity();
        updateFromRequest(request, product);
        return product;
    }

    @Override
    public void updateFromRequest(CreateProductRequest request, ProductEntity product) {
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setSalePrice(request.salePrice());
        product.setCostPrice(request.costPrice());
        product.setStockQuantity(request.stockQuantity());
        product.setReorderLevel(request.reorderLevel());
        product.setMaterial(request.material());
        product.setColors(request.colors());
        product.setBrand(request.brand());
        product.setWarranty(request.warranty());
        product.setIsElectronic(request.isElectronic());
        product.setIsAvailable(request.stockQuantity() > 0);
    }
}
