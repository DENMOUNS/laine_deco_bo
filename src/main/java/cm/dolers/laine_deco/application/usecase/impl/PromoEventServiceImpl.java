package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.PromoEventMapper;
import cm.dolers.laine_deco.application.usecase.PromoEventService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.ValidationException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PromoEventEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.PromoEventJpaRepository;
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
public class PromoEventServiceImpl implements PromoEventService {
    private final PromoEventJpaRepository promoEventRepository;
    private final PromoEventMapper promoEventMapper;

    @Override
    @Transactional
    public PromoEventResponse createPromoEvent(CreatePromoEventRequest request) {
        log.info("Creating promo event: {}", request.name());
        try {
            var promoEvent = new PromoEventEntity();
            promoEvent.setName(request.name());
            promoEvent.setDescription(request.description());
            promoEvent.setDiscountPercentage(request.discountPercentage());
            promoEvent.setDiscountAmount(request.discountAmount());
            promoEvent.setStartDate(request.startDate());
            promoEvent.setEndDate(request.endDate());
            promoEvent.setStatus("PENDING");

            var saved = promoEventRepository.save(promoEvent);
            log.info("Promo event created: {}", saved.getId());
            return promoEventMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating promo event", ex);
            throw new ValidationException(ErrorCode.OPERATION_FAILED, "Error creating promo event", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PromoEventResponse getPromoEventById(Long eventId) {
        var promoEvent = promoEventRepository.findById(eventId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PROMO_NOT_FOUND, "ID: " + eventId));
        return promoEventMapper.toResponse(promoEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromoEventResponse> getAllPromoEvents(Pageable pageable) {
        return promoEventRepository.findAll(pageable).map(promoEventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromoEventResponse> getActivePromoEvents(Pageable pageable) {
        return promoEventRepository.findByStatusAndEndDateAfter("ACTIVE", Instant.now(), pageable)
            .map(promoEventMapper::toResponse);
    }

    @Override
    @Transactional
    public PromoEventResponse updatePromoEvent(Long eventId, CreatePromoEventRequest request) {
        var promoEvent = promoEventRepository.findById(eventId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PROMO_NOT_FOUND, "ID: " + eventId));

        promoEvent.setName(request.name());
        promoEvent.setDescription(request.description());
        promoEvent.setDiscountPercentage(request.discountPercentage());
        promoEvent.setDiscountAmount(request.discountAmount());
        promoEvent.setStartDate(request.startDate());
        promoEvent.setEndDate(request.endDate());

        var updated = promoEventRepository.save(promoEvent);
        log.info("Promo event updated: {}", eventId);
        return promoEventMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deletePromoEvent(Long eventId) {
        if (!promoEventRepository.existsById(eventId)) {
            throw new ValidationException(ErrorCode.PROMO_NOT_FOUND, "ID: " + eventId);
        }
        promoEventRepository.deleteById(eventId);
        log.info("Promo event deleted: {}", eventId);
    }

    @Override
    @Transactional
    public void activatePromoEvent(Long eventId) {
        var promoEvent = promoEventRepository.findById(eventId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PROMO_NOT_FOUND, "ID: " + eventId));
        promoEvent.setStatus("ACTIVE");
        promoEventRepository.save(promoEvent);
        log.info("Promo event activated: {}", eventId);
    }

    @Override
    @Transactional
    public void deactivatePromoEvent(Long eventId) {
        var promoEvent = promoEventRepository.findById(eventId)
            .orElseThrow(() -> new ValidationException(ErrorCode.PROMO_NOT_FOUND, "ID: " + eventId));
        promoEvent.setStatus("INACTIVE");
        promoEventRepository.save(promoEvent);
        log.info("Promo event deactivated: {}", eventId);
    }
}
