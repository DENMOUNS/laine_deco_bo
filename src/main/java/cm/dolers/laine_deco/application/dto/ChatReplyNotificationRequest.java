package cm.dolers.laine_deco.application.dto;

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
