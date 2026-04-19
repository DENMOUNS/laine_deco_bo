package cm.dolers.laine_deco.application.dto;

/**
 * DTO pour créer une notification système (admin)
 */
public record CreateSystemNotificationRequest(
    String targetRole,
    String filterUserIds,
    String type,
    String title,
    String message,
    String icon,
    String actionUrl
) {}
