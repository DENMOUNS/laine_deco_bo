package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.infrastructure.persistence.repository.OrderJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderEntity;
import cm.dolers.laine_deco.application.dto.OrderTrackingResponse;
import cm.dolers.laine_deco.application.mapper.OrderTrackingMapper;
import cm.dolers.laine_deco.application.usecase.OrderTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderTrackingServiceImpl implements OrderTrackingService {
    private final OrderJpaRepository orderRepository;
    private final OrderTrackingMapper mapper;

    @Override
    public OrderTrackingResponse trackOrderByNumber(String orderNumber) {
        OrderEntity order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNumber));
        return mapper.toResponse(order);
    }

    @Override
    public OrderTrackingResponse trackOrderById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        return mapper.toResponse(order);
    }
}
