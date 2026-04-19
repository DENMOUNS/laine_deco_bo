package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service pour Promo Events
 */
public interface PromoEventService {
    PromoEventResponse createPromoEvent(CreatePromoEventRequest request);
    PromoEventResponse getPromoEventById(Long eventId);
    Page<PromoEventResponse> getAllPromoEvents(Pageable pageable);
    Page<PromoEventResponse> getActivePromoEvents(Pageable pageable);
    PromoEventResponse updatePromoEvent(Long eventId, CreatePromoEventRequest request);
    void deletePromoEvent(Long eventId);
    void activatePromoEvent(Long eventId);
    void deactivatePromoEvent(Long eventId);

    org.springframework.data.domain.Page<cm.dolers.laine_deco.application.dto.PromoEventResponse> getFeaturedPromoEvents(org.springframework.data.domain.Pageable pageable);
}


