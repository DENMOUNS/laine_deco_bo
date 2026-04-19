package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

public record ChatConversationResponse(Long id, Long clientId, Long supportAgentId, String status, Instant createdAt, Instant updatedAt) {}
