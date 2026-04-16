package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity;

/**
 * Interface Mapper pour Product
 */
public interface ProductMapper {
    ProductResponse toResponse(ProductEntity product);
    
    ProductDetailResponse toDetailResponse(ProductEntity product);
    
    ProductEntity createFromRequest(CreateProductRequest request);
    
    void updateFromRequest(CreateProductRequest request, ProductEntity product);
}
