package cm.dolers.laine_deco.interfaces.rest;


import cm.dolers.laine_deco.application.dto.LegalPageResponse;
import cm.dolers.laine_deco.application.usecase.LegalPageService;
import cm.dolers.laine_deco.domain.model.LegalPageType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/legal")
@RequiredArgsConstructor
public class PublicLegalPageController {
    private final LegalPageService legalPageService;

    @GetMapping
    public ResponseEntity<List<LegalPageResponse>> getAllPublishedPages() {
        List<LegalPageResponse> pages = legalPageService.getAllPublishedPages();
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{type}")
    public ResponseEntity<LegalPageResponse> getPageByType(@PathVariable String type) {
        LegalPageType pageType = LegalPageType.valueOf(type.toUpperCase());
        LegalPageResponse response = legalPageService.getPageByType(pageType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/terms-of-service")
    public ResponseEntity<LegalPageResponse> getTermsOfService() {
        LegalPageResponse response = legalPageService.getPageByType(LegalPageType.TERMS_OF_SERVICE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/privacy-policy")
    public ResponseEntity<LegalPageResponse> getPrivacyPolicy() {
        LegalPageResponse response = legalPageService.getPageByType(LegalPageType.PRIVACY_POLICY);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/legal-notice")
    public ResponseEntity<LegalPageResponse> getLegalNotice() {
        LegalPageResponse response = legalPageService.getPageByType(LegalPageType.LEGAL_NOTICE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/about-us")
    public ResponseEntity<LegalPageResponse> getAboutUs() {
        LegalPageResponse response = legalPageService.getPageByType(LegalPageType.ABOUT_US);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/site-version")
    public ResponseEntity<LegalPageResponse> getSiteVersion() {
        LegalPageResponse response = legalPageService.getPageByType(LegalPageType.SITE_VERSION);
        return ResponseEntity.ok(response);
    }
}

