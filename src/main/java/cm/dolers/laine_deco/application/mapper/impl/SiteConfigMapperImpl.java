package cm.dolers.laine_deco.application.mapper.impl;


import cm.dolers.laine_deco.application.dto.SiteConfigResponse;
import cm.dolers.laine_deco.application.mapper.SiteConfigMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.SiteConfigEntity;
import org.springframework.stereotype.Component;

@Component
public class SiteConfigMapperImpl implements SiteConfigMapper {
    @Override
    public SiteConfigResponse toResponse(SiteConfigEntity config) {
        return new SiteConfigResponse(config.getId(), config.getKey(), config.getValue(),
            config.getDescription(), config.getUpdatedAt());
    }
}

