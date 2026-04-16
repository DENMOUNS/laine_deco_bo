package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderEntity;

/**
 * Interface Mapper pour Order
 */
public interface OrderMapper {
    OrderResponse toResponse(OrderEntity order);
    
    OrderDetailResponse toDetailResponse(cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity detail);
}
