package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface Service pour Orders
 * Gestion des commandes
 */
public interface OrderService {
    OrderResponse createOrder(Long userId, CreateOrderRequest request);
    
    OrderResponse getOrderById(Long orderId);
    
    Page<OrderResponse> getUserOrders(Long userId, Pageable pageable);
    
    void updateOrderStatus(Long orderId, String status);
    
    void cancelOrder(Long orderId);
}
