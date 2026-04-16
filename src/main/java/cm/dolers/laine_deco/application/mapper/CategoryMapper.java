package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.CategoryEntity;

/**
 * Mapper pour Category
 */
public interface CategoryMapper {
    CategoryResponse toResponse(CategoryEntity category);
}
