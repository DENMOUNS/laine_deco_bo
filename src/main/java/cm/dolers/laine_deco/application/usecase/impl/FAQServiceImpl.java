package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.infrastructure.persistence.repository.FAQRepository;
import cm.dolers.laine_deco.infrastructure.persistence.entity.FAQEntity;
import cm.dolers.laine_deco.application.dto.CreateFAQRequest;
import cm.dolers.laine_deco.application.dto.FAQResponse;
import cm.dolers.laine_deco.application.mapper.FAQMapper;
import cm.dolers.laine_deco.application.usecase.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FAQServiceImpl implements FAQService {
    private final FAQRepository repository;
    private final FAQMapper mapper;

    @Override
    public FAQResponse createFAQ(CreateFAQRequest request) {
        FAQEntity faq = new FAQEntity();
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        faq.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        faq.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        faq.setCreatedAt(Instant.now());
        faq.setUpdatedAt(Instant.now());
        
        FAQEntity saved = repository.save(faq);
        return mapper.toResponse(saved);
    }

    @Override
    public FAQResponse updateFAQ(Long faqId, CreateFAQRequest request) {
        FAQEntity faq = repository.findById(faqId)
            .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + faqId));
        
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        faq.setDisplayOrder(request.getDisplayOrder());
        faq.setIsActive(request.getIsActive());
        faq.setUpdatedAt(Instant.now());
        
        FAQEntity updated = repository.save(faq);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public FAQResponse getFAQById(Long faqId) {
        FAQEntity faq = repository.findById(faqId)
            .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + faqId));
        return mapper.toResponse(faq);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FAQResponse> getAllActiveFAQs() {
        return mapper.toResponseList(repository.findAllByIsActiveTrueOrderByDisplayOrder());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FAQResponse> getAllFAQs() {
        return mapper.toResponseList(repository.findAllOrderByDisplayOrder());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FAQResponse> getAllInactiveFAQs() {
        return mapper.toResponseList(repository.findAllByIsActiveFalse());
    }

    @Override
    public void activateFAQ(Long faqId) {
        FAQEntity faq = repository.findById(faqId)
            .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + faqId));
        faq.setIsActive(true);
        faq.setUpdatedAt(Instant.now());
        repository.save(faq);
    }

    @Override
    public void deactivateFAQ(Long faqId) {
        FAQEntity faq = repository.findById(faqId)
            .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + faqId));
        faq.setIsActive(false);
        faq.setUpdatedAt(Instant.now());
        repository.save(faq);
    }

    @Override
    public void deleteFAQ(Long faqId) {
        repository.deleteById(faqId);
    }

    @Override
    public void updateFAQOrder(Long faqId, Integer newOrder) {
        FAQEntity faq = repository.findById(faqId)
            .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + faqId));
        faq.setDisplayOrder(newOrder);
        faq.setUpdatedAt(Instant.now());
        repository.save(faq);
    }
}
