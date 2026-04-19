package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.NotificationMapper;
import cm.dolers.laine_deco.application.usecase.NotificationService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.UserException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.NotificationEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor

public class NotificationServiceImpl implements NotificationService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final NotificationJpaRepository notificationRepository;
    private final UserJpaRepository userRepository;
    private final ConversationJpaRepository conversationRepository;
    private final OrderJpaRepository orderRepository;
    private final NotificationMapper notificationMapper;

    // ==================== CRUD ====================

    @Override
    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        log.info("Creating notification for user: {}", request.userId());

        var user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User ID: " + request.userId()));

        try {
            var notification = new NotificationEntity();
            notification.setUser(user);
            notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.valueOf(request.type()));
            notification.setTitle(request.title());
            notification.setMessage(request.message());
            notification.setIcon(request.icon());

            notification.setActionUrl(request.actionUrl());
            notification.setIsRead(false);

            var saved = notificationRepository.save(notification);
            log.info("Notification created: {}", saved.getId());
            return notificationMapper.toResponse(saved);
        } catch (Exception ex) {
            log.error("Error creating notification", ex);
            throw new UserException(ErrorCode.OPERATION_FAILED, "Error creating notification", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(Long notificationId) {
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new UserException(ErrorCode.NOTIFICATION_NOT_FOUND, "ID: " + notificationId));
        return notificationMapper.toResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
                .map(notificationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new UserException(ErrorCode.NOTIFICATION_NOT_FOUND, "ID: " + notificationId));
        notification.setIsRead(true);
        notification.setReadAt(Instant.now());
        notificationRepository.save(notification);
        log.info("Notification marked as read: {}", notificationId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadForUser(userId);
        log.info("All notifications marked as read for user: {}", userId);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new UserException(ErrorCode.NOTIFICATION_NOT_FOUND, "ID: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
        log.info("Notification deleted: {}", notificationId);
    }

    // ==================== Notifications spécifiques ====================

    @Override
    @Transactional
    public void notifyOnChatReply(ChatReplyNotificationRequest request) {
        log.info("Notifying chat reply for conversation: {}", request.conversationId());

        var conversation = conversationRepository.findById(request.conversationId())
                .orElseThrow(() -> new UserException(ErrorCode.CHAT_NOT_FOUND,
                        "Conversation ID: " + request.conversationId()));

        try {
            var notification = new NotificationEntity();
            notification.setUser(conversation.getClient());
            notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.ORDER);
            notification.setTitle("Nouvelle réponse au chat");
            notification.setMessage(String.format("%s a répondu: %s", request.repliedByName(),
                    request.message().substring(0, Math.min(50, request.message().length())) + "..."));
            notification.setIcon("💬");

            notification.setActionUrl("/chat/conversations/" + request.conversationId());
            notification.setIsRead(false);

            notificationRepository.save(notification);
            log.info("Chat reply notification created for user: {}", conversation.getClient().getId());
        } catch (Exception ex) {
            log.error("Error notifying chat reply", ex);
        }
    }

    @Override
    @Transactional
    public void notifyOrderStatusChange(OrderStatusChangeNotificationRequest request) {
        log.info("Notifying order status change for order: {}", request.orderId());

        var order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new UserException(ErrorCode.ORDER_NOT_FOUND, "Order ID: " + request.orderId()));

        try {
            var notification = new NotificationEntity();
            notification.setUser(order.getClient());
            notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.ORDER);
            notification.setTitle("Statut de commande mis à jour");
            notification.setMessage(String.format("Votre commande est passée de %s à %s",
                    request.oldStatus(), request.newStatus()));

            // Icônes selon le statut
            var icon = switch (request.newStatus()) {
                case "CONFIRMED" -> "✅";
                case "PREPARING" -> "📦";
                case "SHIPPED" -> "🚚";
                case "DELIVERED" -> "🎉";
                case "CANCELLED" -> "❌";
                default -> "📋";
            };

            notification.setIcon(icon);

            notification.setActionUrl("/orders/" + request.orderId());
            notification.setIsRead(false);

            notificationRepository.save(notification);
            log.info("Order status change notification created for user: {}", order.getClient().getId());
        } catch (Exception ex) {
            log.error("Error notifying order status change", ex);
        }
    }

    @Override
    @Transactional
    public void broadcastSystemNotification(CreateSystemNotificationRequest request) {
        log.info("Broadcasting system notification to: {}", request.targetRole());

        try {
            List<cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity> targetUsers;

            if ("ALL".equals(request.targetRole())) {
                targetUsers = userRepository.findAll();
            } else if ("ADMIN".equals(request.targetRole())) {
                targetUsers = userRepository.findByRole(cm.dolers.laine_deco.domain.model.Role.ADMIN);
            } else if ("FINANCE".equals(request.targetRole())) {
                targetUsers = userRepository.findByRole(cm.dolers.laine_deco.domain.model.Role.FINANCE);
            } else if ("CLIENT".equals(request.targetRole())) {
                targetUsers = userRepository.findByRole(cm.dolers.laine_deco.domain.model.Role.CLIENT);
            } else {
                targetUsers = List.of();
            }

            for (var user : targetUsers) {
                var notification = new NotificationEntity();
                notification.setUser(user);
                notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.CUSTOMER);
                notification.setTitle(request.title());
                notification.setMessage(request.message());
                notification.setIcon(request.icon());
                notification.setActionUrl(request.actionUrl());
                notification.setIsRead(false);

                notificationRepository.save(notification);
            }

            log.info("System notification broadcasted to {} users", targetUsers.size());
        } catch (Exception ex) {
            log.error("Error broadcasting system notification", ex);
        }
    }

    @Override
    @Transactional
    public void notifyBadgeEarned(Long userId, String badgeName, String badgeDescription) {
        log.info("Notifying badge earned for user: {} - Badge: {}", userId, badgeName);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        try {
            var notification = new NotificationEntity();
            notification.setUser(user);
            notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.CUSTOMER);
            notification.setTitle("🏆 Badge obtenu!");
            notification.setMessage(String.format("Vous avez obtenu le badge: %s - %s", badgeName, badgeDescription));
            notification.setIcon("🏆");

            notification.setActionUrl("/badges/" + badgeName);
            notification.setIsRead(false);

            notificationRepository.save(notification);
        } catch (Exception ex) {
            log.error("Error notifying badge earned", ex);
        }
    }

    @Override
    @Transactional
    public void notifyCouponAvailable(Long userId, String couponCode, String discount) {
        log.info("Notifying coupon available for user: {} - Code: {}", userId, couponCode);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        try {
            var notification = new NotificationEntity();
            notification.setUser(user);
            notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.ORDER);
            notification.setTitle("🎉 Coupon disponible!");
            notification.setMessage(String.format("Utilisez le code %s pour %s de réduction!", couponCode, discount));
            notification.setIcon("🎉");

            notification.setActionUrl("/coupons/" + couponCode);
            notification.setIsRead(false);

            notificationRepository.save(notification);
        } catch (Exception ex) {
            log.error("Error notifying coupon available", ex);
        }
    }

    @Override
    @Transactional
    public void notifyPromoEventActivated(String eventName, String description) {
        log.info("Notifying all users about promo event: {}", eventName);

        try {
            var users = userRepository.findByRole(cm.dolers.laine_deco.domain.model.Role.CLIENT);

            for (var user : users) {
                var notification = new NotificationEntity();
                notification.setUser(user);
                notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.ORDER);
                notification.setTitle("⭐ Événement promotionnel!");
                notification.setMessage(String.format("%s - %s", eventName, description));
                notification.setIcon("⭐");
                notification.setActionUrl("/promo-events");
                notification.setIsRead(false);

                notificationRepository.save(notification);
            }

            log.info("Promo event notification broadcasted to {} users", users.size());
        } catch (Exception ex) {
            log.error("Error notifying promo event activation", ex);
        }
    }

    @Override
    @Transactional
    public void notifyRmaApproved(Long userId, String rmaNumber) {
        log.info("Notifying RMA approved for user: {} - RMA: {}", userId, rmaNumber);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        try {
            var notification = new NotificationEntity();
            notification.setUser(user);
            notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.ORDER);
            notification.setTitle("✅ Retour approuvé");
            notification.setMessage(String
                    .format("Votre demande de retour %s a été approuvée. Veuillez préparer le colis.", rmaNumber));
            notification.setIcon("✅");
            // notification.setRelatedId(rmaNumber);
            notification.setActionUrl("/rma/" + rmaNumber);
            notification.setIsRead(false);

            notificationRepository.save(notification);
        } catch (Exception ex) {
            log.error("Error notifying RMA approved", ex);
        }
    }

    @Override
    @Transactional
    public void notifyRmaRejected(Long userId, String rmaNumber, String reason) {
        log.info("Notifying RMA rejected for user: {} - RMA: {}", userId, rmaNumber);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        try {
            var notification = new NotificationEntity();
            notification.setUser(user);
            notification.setType(cm.dolers.laine_deco.domain.model.NotificationType.ORDER);
            notification.setTitle("❌ Retour rejeté");
            notification.setMessage(
                    String.format("Votre demande de retour %s a été rejetée. Raison: %s", rmaNumber, reason));
            notification.setIcon("❌");
            // notification.setRelatedId(rmaNumber);
            notification.setActionUrl("/rma/" + rmaNumber);
            notification.setIsRead(false);

            notificationRepository.save(notification);
        } catch (Exception ex) {
            log.error("Error notifying RMA rejected", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByType(Long userId, String type, Pageable pageable) {
        log.info("Getting notifications by type: {} for user: {}", type, userId);
        return notificationRepository
                .findByUserIdAndType(userId, cm.dolers.laine_deco.domain.model.NotificationType.valueOf(type), pageable)
                .map(notificationMapper::toResponse);
    }

    @Override
    public void cleanOldNotifications(int daysOld) {
        log.info("Cleaning notifications older than {} days", daysOld);
        java.time.Instant threshold = java.time.Instant.now().minus(java.time.Duration.ofDays(daysOld));
        notificationRepository.deleteByCreatedAtBefore(threshold);
    }
}
