package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

/**
 * DTO pour les notifications
 * Types supportés:
 * - CHAT_MESSAGE_REPLY: Réponse à un message du chat
 * - ORDER_STATUS_CHANGED: Changement de statut de commande
 * - SYSTEM_ANNOUNCEMENT: Annonce système par admin
 * - BADGE_EARNED: Badge obtenu
 * - COUPON_AVAILABLE: Coupon disponible
 */
public record NotificationResponse(
    Long id,
    Long userId,
    String type,                    // CHAT_MESSAGE_REPLY, ORDER_STATUS_CHANGED, SYSTEM_ANNOUNCEMENT, etc.
    String title,
    String message,
    String icon,                    // emoji ou URL icône
    String relatedId,              // ID du chat, commande, etc.
    String actionUrl,              // lien pour action directe
    Boolean isRead,
    Instant createdAt,
    Instant readAt
) {}

/**
 * DTO pour créer une notification
 */
public record CreateNotificationRequest(
    Long userId,
    String type,
    String title,
    String message,
    String icon,
    String relatedId,
    String actionUrl
) {}

/**
 * DTO pour créer une notification par admin (système)
 */
public record CreateSystemNotificationRequest(
    String targetRole,             // ALL, ADMIN, FINANCE, CLIENT
    String filterUserIds,          // comma-separated ou null pour tous
    String type,
    String title,
    String message,
    String icon,
    String actionUrl
) {}

/**
 * DTO pour le changement de statut commande (déclenche notification)
 */
public record OrderStatusChangeNotificationRequest(
    Long orderId,
    String oldStatus,
    String newStatus,
    String details
) {}

/**
 * DTO pour notification de réponse au chat
 */
public record ChatReplyNotificationRequest(
    Long conversationId,
    Long repliedBy,
    String repliedByName,
    String repliedByRole,
    String message,
    Boolean isFromAdmin
) {}
