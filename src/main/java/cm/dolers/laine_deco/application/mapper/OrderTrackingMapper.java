package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderEntity;
import cm.dolers.laine_deco.application.dto.OrderTrackingResponse;

public interface OrderTrackingMapper {
    OrderTrackingResponse toResponse(OrderEntity entity);
}
