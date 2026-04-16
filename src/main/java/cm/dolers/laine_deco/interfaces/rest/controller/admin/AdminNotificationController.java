package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller pour les Notifications
 * Responsabilité: Créer et gérer les notifications système/broadcasts
 */
@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminNotificationController {
    private final NotificationService notificationService;

    @PostMapping("/system-broadcast")
    public ResponseEntity<Void> sendSystemBroadcast(@Valid @RequestBody CreateSystemNotificationRequest request) {
        log.info("POST /api/admin/notifications/system-broadcast - Target: {}", request.targetRole());
        notificationService.broadcastSystemNotification(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order-status-change")
    public ResponseEntity<Void> notifyOrderStatusChange(@Valid @RequestBody OrderStatusChangeNotificationRequest request) {
        log.info("POST /api/admin/notifications/order-status-change - Order: {}", request.orderId());
        notificationService.notifyOrderStatusChange(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/badge-earned")
    public ResponseEntity<Void> notifyBadgeEarned(
            @RequestParam Long userId,
            @RequestParam String badgeName,
            @RequestParam String badgeDescription) {
        log.info("POST /api/admin/notifications/badge-earned - User: {}, Badge: {}", userId, badgeName);
        notificationService.notifyBadgeEarned(userId, badgeName, badgeDescription);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/coupon-available")
    public ResponseEntity<Void> notifyCouponAvailable(
            @RequestParam Long userId,
            @RequestParam String couponCode,
            @RequestParam String discount) {
        log.info("POST /api/admin/notifications/coupon-available - User: {}, Code: {}", userId, couponCode);
        notificationService.notifyCouponAvailable(userId, couponCode, discount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/promo-event-activated")
    public ResponseEntity<Void> notifyPromoEventActivated(
            @RequestParam String eventName,
            @RequestParam String description) {
        log.info("POST /api/admin/notifications/promo-event-activated - Event: {}", eventName);
        notificationService.notifyPromoEventActivated(eventName, description);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rma-approved")
    public ResponseEntity<Void> notifyRmaApproved(
            @RequestParam Long userId,
            @RequestParam String rmaNumber) {
        log.info("POST /api/admin/notifications/rma-approved - RMA: {}", rmaNumber);
        notificationService.notifyRmaApproved(userId, rmaNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rma-rejected")
    public ResponseEntity<Void> notifyRmaRejected(
            @RequestParam Long userId,
            @RequestParam String rmaNumber,
            @RequestParam String reason) {
        log.info("POST /api/admin/notifications/rma-rejected - RMA: {}", rmaNumber);
        notificationService.notifyRmaRejected(userId, rmaNumber, reason);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<NotificationResponse>> getUserNotifications(
            @PathVariable Long userId,
            Pageable pageable) {
        log.info("GET /api/admin/notifications/user/{}", userId);
        var response = notificationService.getUserNotifications(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long notificationId) {
        log.info("GET /api/admin/notifications/{}", notificationId);
        var response = notificationService.getNotificationById(notificationId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        log.info("POST /api/admin/notifications/{}/mark-as-read", notificationId);
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        log.info("DELETE /api/admin/notifications/{}", notificationId);
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
    }
}
