package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service pour Knitting Patterns
 */
public interface KnittingPatternService {
    KnittingPatternResponse createPattern(CreateKnittingPatternRequest request);
    KnittingPatternResponse getPatternById(Long patternId);
    Page<KnittingPatternResponse> getAllPatterns(Pageable pageable);
    Page<KnittingPatternResponse> searchPatterns(String keyword, Pageable pageable);
    Page<KnittingPatternResponse> getPatternsBySkillLevel(String level, Pageable pageable);
    KnittingPatternResponse updatePattern(Long patternId, CreateKnittingPatternRequest request);
    void deletePattern(Long patternId);
    void incrementDownloadCount(Long patternId);

    org.springframework.data.domain.Page<cm.dolers.laine_deco.application.dto.KnittingPatternResponse> getPublishedPatterns(org.springframework.data.domain.Pageable pageable);
}


