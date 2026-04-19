package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.PaymentResponse;
import cm.dolers.laine_deco.application.mapper.PaymentMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponse toResponse(PaymentEntity entity) {
        return new PaymentResponse(
            entity.getId(),
            entity.getOrder().getId(),
            entity.getAmount(),
            entity.getMethod(),
            entity.getStatus(),
            entity.getTransactionId(),
            entity.getProcessedAt()
        );
    }
}

