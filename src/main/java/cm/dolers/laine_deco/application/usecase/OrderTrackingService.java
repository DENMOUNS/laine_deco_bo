package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.OrderTrackingResponse;

public interface OrderTrackingService {
    OrderTrackingResponse trackOrderByNumber(String orderNumber);
    OrderTrackingResponse trackOrderById(Long orderId);
}

