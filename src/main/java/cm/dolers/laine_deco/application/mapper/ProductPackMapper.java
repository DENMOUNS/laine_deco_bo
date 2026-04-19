package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductPackEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ProductPackProductEntity;
import java.util.List;

public interface ProductPackMapper {
    ProductPackResponse toResponse(ProductPackEntity entity);
    List<ProductPackResponse> toResponseList(List<ProductPackEntity> entities);
    ProductPackItemDto toItemDto(ProductPackProductEntity entity);
}
