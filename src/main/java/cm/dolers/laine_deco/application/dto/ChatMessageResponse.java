package cm.dolers.laine_deco.application.dto;

public record ChatMessageResponse(Long id, Long conversationId, Long senderId, String senderName, String content, Boolean isRead, java.time.Instant createdAt) {}
