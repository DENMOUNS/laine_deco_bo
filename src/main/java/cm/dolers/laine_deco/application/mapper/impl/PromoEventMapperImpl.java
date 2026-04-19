package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.PromoEventResponse;
import cm.dolers.laine_deco.application.mapper.PromoEventMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PromoEventEntity;
import org.springframework.stereotype.Component;

@Component
public class PromoEventMapperImpl implements PromoEventMapper {
    @Override
    public PromoEventResponse toResponse(PromoEventEntity event) {
        return new PromoEventResponse(event.getId(), event.getName(), event.getDescription(),
            event.getDiscountPercentage(), event.getDiscountAmount(), event.getStartDate(),
            event.getEndDate(), event.getStatus(), event.getCreatedAt());
    }
}

