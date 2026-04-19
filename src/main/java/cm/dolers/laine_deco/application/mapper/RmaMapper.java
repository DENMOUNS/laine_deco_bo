package cm.dolers.laine_deco.application.mapper;


import cm.dolers.laine_deco.application.dto.RmaResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.RMAEntity;

public interface RmaMapper {
    RmaResponse toResponse(RMAEntity rma);
}


