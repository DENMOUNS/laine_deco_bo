package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.NotificationResponse;
import cm.dolers.laine_deco.application.usecase.NotificationService;
import cm.dolers.laine_deco.infrastructure.config.PaginationConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Client Controller pour les Notifications
 * Responsabilité: Consulter et gérer les notifications personnelles
 */
@RestController
@RequestMapping("/api/client/notifications")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientNotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(Pageable pageable) {
        Long userId = extractUserIdFromToken();
        log.info("GET /api/client/notifications - User: {}", userId);
        var response = notificationService.getUserNotifications(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread")
    public ResponseEntity<Page<NotificationResponse>> getUnreadNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = extractUserIdFromToken();
        
        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        log.info("GET /api/client/notifications/unread - User: {}, page: {}, size: {}", userId, page, normalizedSize);
        
        List<NotificationResponse> results = notificationService.getUnreadNotifications(userId);
        
        // Appliquer la pagination manuellement
        int start = page * normalizedSize;
        int end = Math.min(start + normalizedSize, results.size());
        List<NotificationResponse> pageContent = results.subList(start, end);
        
        Page<NotificationResponse> pageResult = new PageImpl<>(
            pageContent,
            org.springframework.data.domain.PageRequest.of(page, normalizedSize),
            results.size()
        );
        
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        Long userId = extractUserIdFromToken();
        log.info("GET /api/client/notifications/unread-count - User: {}", userId);
        var count = notificationService.getUnreadNotificationCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsByType(
            @PathVariable String type,
            Pageable pageable) {
        Long userId = extractUserIdFromToken();
        log.info("GET /api/client/notifications/type/{} - User: {}", type, userId);
        var response = notificationService.getNotificationsByType(userId, type, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long id) {
        log.info("GET /api/client/notifications/{}", id);
        var response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/mark-as-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("POST /api/client/notifications/{}/mark-as-read", id);
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-all-as-read")
    public ResponseEntity<Void> markAllAsRead() {
        Long userId = extractUserIdFromToken();
        log.info("POST /api/client/notifications/mark-all-as-read - User: {}", userId);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.info("DELETE /api/client/notifications/{}", id);
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken() {
        // TODO: Extraire correctement du token JWT
        return 1L;
    }
}
        return ResponseEntity.ok(count);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("POST /api/client/notifications/{}/read", id);
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("POST /api/client/notifications/read-all - User: {}", userId);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.info("DELETE /api/client/notifications/{}", id);
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        // TODO: Implémenter correctement
        return 1L;
    }
}
