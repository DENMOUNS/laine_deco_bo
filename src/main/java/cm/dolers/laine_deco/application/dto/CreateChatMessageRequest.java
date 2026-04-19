package cm.dolers.laine_deco.application.dto;

/**
 * DTO pour créer un message de chat
 */
public record CreateChatMessageRequest(
    Long conversationId,
    String message,
    String senderType
) {}
