package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.infrastructure.persistence.repository.LegalPageRepository;
import cm.dolers.laine_deco.infrastructure.persistence.entity.LegalPageEntity;
import cm.dolers.laine_deco.domain.model.LegalPageType;
import cm.dolers.laine_deco.application.dto.CreateLegalPageRequest;
import cm.dolers.laine_deco.application.dto.LegalPageResponse;
import cm.dolers.laine_deco.application.mapper.LegalPageMapper;
import cm.dolers.laine_deco.application.usecase.LegalPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LegalPageServiceImpl implements LegalPageService {
    private final LegalPageRepository repository;
    private final LegalPageMapper mapper;

    @Override
    public LegalPageResponse createOrUpdatePage(LegalPageType type, CreateLegalPageRequest request) {
        LegalPageEntity page = repository.findByType(type)
            .orElse(new LegalPageEntity());
        
        page.setType(type);
        page.setTitle(request.getTitle());
        page.setContent(request.getContent());
        page.setSummary(request.getSummary());
        page.setVersion(request.getVersion());
        page.setUpdatedAt(Instant.now());
        
        if (page.getId() == null) {
            page.setCreatedAt(Instant.now());
        }
        
        LegalPageEntity saved = repository.save(page);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LegalPageResponse getPageByType(LegalPageType type) {
        LegalPageEntity page = repository.findByType(type)
            .orElseThrow(() -> new IllegalArgumentException("Legal page not found: " + type));
        return mapper.toResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalPageResponse> getAllPublishedPages() {
        return mapper.toResponseList(repository.findAllByIsPublishedTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalPageResponse> getAllPages() {
        return mapper.toResponseList(repository.findAllByOrderByUpdatedAtDesc());
    }

    @Override
    public LegalPageResponse publishPage(LegalPageType type) {
        LegalPageEntity page = repository.findByType(type)
            .orElseThrow(() -> new IllegalArgumentException("Legal page not found: " + type));
        
        page.setIsPublished(true);
        page.setPublishedAt(Instant.now());
        page.setUpdatedAt(Instant.now());
        
        LegalPageEntity updated = repository.save(page);
        return mapper.toResponse(updated);
    }

    @Override
    public LegalPageResponse unpublishPage(LegalPageType type) {
        LegalPageEntity page = repository.findByType(type)
            .orElseThrow(() -> new IllegalArgumentException("Legal page not found: " + type));
        
        page.setIsPublished(false);
        page.setUpdatedAt(Instant.now());
        
        LegalPageEntity updated = repository.save(page);
        return mapper.toResponse(updated);
    }

    @Override
    public void deletePage(Long pageId) {
        repository.deleteById(pageId);
    }
}
