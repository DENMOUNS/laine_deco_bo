package cm.dolers.laine_deco.application.dto;
public record CreateNotificationRequest(Long userId, String type, String title, String message, String icon, String actionUrl, Long relatedId) {}
