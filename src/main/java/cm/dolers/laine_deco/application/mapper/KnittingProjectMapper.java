package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.KnittingProjectResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.KnittingProjectEntity;

public interface KnittingProjectMapper {
    KnittingProjectResponse toResponse(KnittingProjectEntity entity);
}

