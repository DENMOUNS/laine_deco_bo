package cm.dolers.laine_deco.application.mapper;


import cm.dolers.laine_deco.application.dto.PaymentResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.PaymentEntity;

public interface PaymentMapper {
    PaymentResponse toResponse(PaymentEntity entity);
}

