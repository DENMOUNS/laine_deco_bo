package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.infrastructure.persistence.entity.LegalPageEntity;
import cm.dolers.laine_deco.application.dto.LegalPageResponse;

import java.util.List;

public interface LegalPageMapper {
    LegalPageResponse toResponse(LegalPageEntity entity);
    List<LegalPageResponse> toResponseList(List<LegalPageEntity> entities);
}
