package cm.dolers.laine_deco.application.dto;

public record NotificationResponse(Long id, Long userId, cm.dolers.laine_deco.domain.model.NotificationType type, String title, String message, Long referenceId, Boolean isRead, java.time.Instant createdAt) {}
