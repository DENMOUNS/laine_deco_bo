package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.OrderTrackingResponse;
import cm.dolers.laine_deco.application.usecase.OrderTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/order-tracking")
@RequiredArgsConstructor
public class PublicOrderTrackingController {
    private final OrderTrackingService orderTrackingService;

    @GetMapping("/by-number/{orderNumber}")
    public ResponseEntity<OrderTrackingResponse> trackOrderByNumber(@PathVariable String orderNumber) {
        OrderTrackingResponse response = orderTrackingService.trackOrderByNumber(orderNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-id/{orderId}")
    public ResponseEntity<OrderTrackingResponse> trackOrderById(@PathVariable Long orderId) {
        OrderTrackingResponse response = orderTrackingService.trackOrderById(orderId);
        return ResponseEntity.ok(response);
    }
}

