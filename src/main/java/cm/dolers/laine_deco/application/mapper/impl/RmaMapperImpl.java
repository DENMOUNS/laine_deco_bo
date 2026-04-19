package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.RmaResponse;
import cm.dolers.laine_deco.application.mapper.RmaMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.RMAEntity;
import org.springframework.stereotype.Component;

@Component
public class RmaMapperImpl implements RmaMapper {
    @Override
    public RmaResponse toResponse(RMAEntity rma) {
        return new RmaResponse(rma.getId(), rma.getOrder().getId(), rma.getRmaNumber(),
            rma.getReason(), rma.getStatus().name(), rma.getTrackingNumber(), rma.getCreatedAt(), rma.getResolvedAt());
    }
}



