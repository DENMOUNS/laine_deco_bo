package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.SiteConfigMapper;
import cm.dolers.laine_deco.application.usecase.SiteConfigService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.SiteConfigEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.SiteConfigJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class SiteConfigServiceImpl implements SiteConfigService {
    private final SiteConfigJpaRepository configRepository;
    private final SiteConfigMapper configMapper;

    @Override
    @Transactional(readOnly = true)
    public SiteConfigResponse getConfigByKey(String key) {
        var config = configRepository.findByKey(key)
            .orElseThrow(() -> new ValidationException(ErrorCode.CONFIG_NOT_FOUND, "Key: " + key));
        return configMapper.toResponse(config);
    }

    @Override
    @Transactional
    public SiteConfigResponse updateConfig(UpdateSiteConfigRequest request) {
        log.info("Updating config: {}", request.key());

        var config = configRepository.findByKey(request.key())
            .orElse(new SiteConfigEntity());

        config.setKey(request.key());
        config.setValue(request.value());

        var updated = configRepository.save(config);
        log.info("Config updated: {}", request.key());
        return configMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteConfigResponse> getAllConfigs(Pageable pageable) {
        return configRepository.findAll(pageable).map(configMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public String getConfigValue(String key) {
        return configRepository.findByKey(key)
            .map(SiteConfigEntity::getValue)
            .orElse(null);
    }

    @Override
    @Transactional
    public void setConfigValue(String key, String value) {
        var config = configRepository.findByKey(key)
            .orElse(new SiteConfigEntity());

        config.setKey(key);
        config.setValue(value);

        configRepository.save(config);
        log.info("Config value set: {} = {}", key, value);
    }
}
