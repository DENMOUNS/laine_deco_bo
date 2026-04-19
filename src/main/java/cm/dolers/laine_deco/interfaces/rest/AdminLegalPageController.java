package cm.dolers.laine_deco.interfaces.rest;


import cm.dolers.laine_deco.application.dto.CreateLegalPageRequest;
import cm.dolers.laine_deco.application.dto.LegalPageResponse;
import cm.dolers.laine_deco.application.usecase.LegalPageService;
import cm.dolers.laine_deco.domain.model.LegalPageType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/legal")
@RequiredArgsConstructor
public class AdminLegalPageController {
    private final LegalPageService legalPageService;

    @PostMapping("/{type}")
    public ResponseEntity<LegalPageResponse> createOrUpdatePage(
            @PathVariable String type,
            @RequestBody CreateLegalPageRequest request) {
        LegalPageType pageType = LegalPageType.valueOf(type.toUpperCase());
        LegalPageResponse response = legalPageService.createOrUpdatePage(pageType, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{type}")
    public ResponseEntity<LegalPageResponse> updatePage(
            @PathVariable String type,
            @RequestBody CreateLegalPageRequest request) {
        LegalPageType pageType = LegalPageType.valueOf(type.toUpperCase());
        LegalPageResponse response = legalPageService.createOrUpdatePage(pageType, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LegalPageResponse>> getAllPages() {
        List<LegalPageResponse> pages = legalPageService.getAllPages();
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{type}")
    public ResponseEntity<LegalPageResponse> getPageByType(@PathVariable String type) {
        LegalPageType pageType = LegalPageType.valueOf(type.toUpperCase());
        LegalPageResponse response = legalPageService.getPageByType(pageType);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{type}/publish")
    public ResponseEntity<LegalPageResponse> publishPage(@PathVariable String type) {
        LegalPageType pageType = LegalPageType.valueOf(type.toUpperCase());
        LegalPageResponse response = legalPageService.publishPage(pageType);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{type}/unpublish")
    public ResponseEntity<LegalPageResponse> unpublishPage(@PathVariable String type) {
        LegalPageType pageType = LegalPageType.valueOf(type.toUpperCase());
        LegalPageResponse response = legalPageService.unpublishPage(pageType);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{pageId}")
    public ResponseEntity<Void> deletePage(@PathVariable Long pageId) {
        legalPageService.deletePage(pageId);
        return ResponseEntity.noContent().build();
    }
}

