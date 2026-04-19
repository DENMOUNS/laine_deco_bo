package cm.dolers.laine_deco.application.dto;

/**
 * DTO pour notification de changement de statut de commande
 */
public record OrderStatusChangeNotificationRequest(
    Long orderId,
    String oldStatus,
    String newStatus,
    String details
) {}
