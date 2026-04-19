package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service pour Site Configuration
 */
public interface SiteConfigService {
    SiteConfigResponse getConfigByKey(String key);
    SiteConfigResponse updateConfig(UpdateSiteConfigRequest request);
    Page<SiteConfigResponse> getAllConfigs(Pageable pageable);
    String getConfigValue(String key);
    void setConfigValue(String key, String value);
}

