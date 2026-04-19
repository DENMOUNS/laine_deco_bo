package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.SiteConfigResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.SiteConfigEntity;

public interface SiteConfigMapper {
    SiteConfigResponse toResponse(SiteConfigEntity config);
}
