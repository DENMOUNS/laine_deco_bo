package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.PromoEventResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PromoEventEntity;

public interface PromoEventMapper {
    PromoEventResponse toResponse(PromoEventEntity event);
}
