package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.CreateLegalPageRequest;
import cm.dolers.laine_deco.application.dto.LegalPageResponse;
import cm.dolers.laine_deco.domain.model.LegalPageType;

import java.util.List;

public interface LegalPageService {
    LegalPageResponse createOrUpdatePage(LegalPageType type, CreateLegalPageRequest request);
    LegalPageResponse getPageByType(LegalPageType type);
    List<LegalPageResponse> getAllPublishedPages();
    List<LegalPageResponse> getAllPages();
    LegalPageResponse publishPage(LegalPageType type);
    LegalPageResponse unpublishPage(LegalPageType type);
    void deletePage(Long pageId);
}
