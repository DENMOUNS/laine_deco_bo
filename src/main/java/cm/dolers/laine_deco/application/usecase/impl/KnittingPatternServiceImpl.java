package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.KnittingPatternMapper;
import cm.dolers.laine_deco.application.usecase.KnittingPatternService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.KnittingPatternEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.KnittingPatternJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KnittingPatternServiceImpl implements KnittingPatternService {
    private final KnittingPatternJpaRepository patternRepository;
    private final KnittingPatternMapper patternMapper;

    @Override
    @Transactional
    public KnittingPatternResponse createPattern(CreateKnittingPatternRequest request) {
        log.info("Creating knitting pattern: {}", request.name());
        try {
            var pattern = new KnittingPatternEntity();
            pattern.setName(request.name());
            pattern.setAuthor(request.author());
            pattern.setDescription(request.description());
            pattern.setSkillLevel(request.skillLevel());
            pattern.setEstimatedHours(request.estimatedHours());
            pattern.setYarnType(request.yarnType());
            pattern.setNeedleSize(request.needleSize());
            pattern.setUrl(request.url());
            pattern.setDownloadCount(0);

            var saved = patternRepository.save(pattern);
            log.info("Knitting pattern created: {}", saved.getId());
            return patternMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating pattern", ex);
            throw new ValidationException(ErrorCode.OPERATION_FAILED, "Error creating pattern", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public KnittingPatternResponse getPatternById(Long patternId) {
        var pattern = patternRepository.findById(patternId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PATTERN_NOT_FOUND, "ID: " + patternId));
        return patternMapper.toResponse(pattern);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KnittingPatternResponse> getAllPatterns(Pageable pageable) {
        return patternRepository.findAll(pageable).map(patternMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KnittingPatternResponse> searchPatterns(String keyword, Pageable pageable) {
        return patternRepository.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword, pageable)
            .map(patternMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KnittingPatternResponse> getPatternsBySkillLevel(String level, Pageable pageable) {
        return patternRepository.findBySkillLevel(level, pageable).map(patternMapper::toResponse);
    }

    @Override
    @Transactional
    public KnittingPatternResponse updatePattern(Long patternId, CreateKnittingPatternRequest request) {
        var pattern = patternRepository.findById(patternId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PATTERN_NOT_FOUND, "ID: " + patternId));

        pattern.setName(request.name());
        pattern.setAuthor(request.author());
        pattern.setDescription(request.description());
        pattern.setSkillLevel(request.skillLevel());
        pattern.setEstimatedHours(request.estimatedHours());
        pattern.setYarnType(request.yarnType());
        pattern.setNeedleSize(request.needleSize());
        pattern.setUrl(request.url());

        var updated = patternRepository.save(pattern);
        log.info("Knitting pattern updated: {}", patternId);
        return patternMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deletePattern(Long patternId) {
        if (!patternRepository.existsById(patternId)) {
            throw new ValidationException(ErrorCode.PATTERN_NOT_FOUND, "ID: " + patternId);
        }
        patternRepository.deleteById(patternId);
        log.info("Knitting pattern deleted: {}", patternId);
    }

    @Override
    @Transactional
    public void incrementDownloadCount(Long patternId) {
        var pattern = patternRepository.findById(patternId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PATTERN_NOT_FOUND, "ID: " + patternId));
        pattern.setDownloadCount(pattern.getDownloadCount() + 1);
        patternRepository.save(pattern);
    }
}
