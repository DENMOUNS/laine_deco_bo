package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderStatusHistoryEntity;
import cm.dolers.laine_deco.application.dto.OrderTrackingResponse;
import cm.dolers.laine_deco.application.mapper.OrderTrackingMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderTrackingMapperImpl implements OrderTrackingMapper {
    @Override
    public OrderTrackingResponse toResponse(OrderEntity entity) {
        OrderTrackingResponse response = new OrderTrackingResponse();
        response.setOrderId(entity.getId());
        response.setOrderNumber(entity.getOrderNumber());
        response.setStatus(entity.getStatus().name());
        response.setTotalAmount(entity.getTotal().longValue());
        response.setItemsCount(entity.getDetails().size());
        response.setCreatedAt(entity.getCreatedAt().toString());
        
        // Estimation livraison: 5 jours après création
        Instant estimatedDelivery = entity.getCreatedAt().plus(5, ChronoUnit.DAYS);
        response.setEstimatedDeliveryDate(estimatedDelivery.toString());
        
        // Historique des statuts (décroissant: récent → ancien)
        List<OrderTrackingResponse.OrderStatusHistoryDto> history = entity.getStatusHistory().stream()
            .sorted((a, b) -> b.getChangedAt().compareTo(a.getChangedAt()))
            .map(sh -> new OrderTrackingResponse.OrderStatusHistoryDto(
                sh.getStatus().name(),
                sh.getChangedAt().toString(),
                sh.getComment(),
                sh.getChangedBy()
            ))
            .collect(Collectors.toList());
        
        response.setStatusHistory(history);
        return response;
    }
}
