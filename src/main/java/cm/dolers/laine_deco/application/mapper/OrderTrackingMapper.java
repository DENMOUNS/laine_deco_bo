package cm.dolers.laine_deco.application.mapper;


import cm.dolers.laine_deco.application.dto.OrderTrackingResponse;
import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderEntity;

public interface OrderTrackingMapper {
    OrderTrackingResponse toResponse(OrderEntity entity);
}

