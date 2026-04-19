package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.SiteConfigService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/site-config")
@RequiredArgsConstructor

@PreAuthorize("hasRole('ADMIN')")
public class AdminSiteConfigController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminSiteConfigController.class);
    private final SiteConfigService siteConfigService;

    @GetMapping("/{key}")
    public ResponseEntity<SiteConfigResponse> getConfig(@PathVariable String key) {
        log.info("GET /api/admin/site-config/{}", key);
        var response = siteConfigService.getConfigByKey(key);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<SiteConfigResponse>> getAllConfigs(Pageable pageable) {
        log.info("GET /api/admin/site-config");
        var response = siteConfigService.getAllConfigs(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<SiteConfigResponse> updateConfig(@Valid @RequestBody UpdateSiteConfigRequest request) {
        log.info("PUT /api/admin/site-config - Key: {}", request.key());
        var response = siteConfigService.updateConfig(request);
        return ResponseEntity.ok(response);
    }
}
