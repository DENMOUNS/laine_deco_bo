package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

/**
 * DTOs pour le Chat client/support
 * Support messages bidirectionnels: USER ↔ ADMIN/FINANCE
 */
public record ChatMessageResponse(
    Long id,
    Long conversationId,
    Long senderId,
    String senderName,
    String senderType,  // USER, ADMIN, FINANCE
    String message,
    Boolean isRead,
    Instant createdAt,
    Instant readAt
) {}

public record CreateChatMessageRequest(
    Long conversationId,
    String message,
    String senderType   // USER, ADMIN, FINANCE
) {}

public record ChatConversationResponse(
    Long id,
    Long clientId,
    String clientName,
    Long assignedAgentId,
    String assignedAgentName,
    String assignedAgentRole,  // ADMIN, FINANCE
    String status,             // OPEN, PENDING, IN_PROGRESS, RESOLVED, CLOSED
    Integer unreadCount,
    Instant createdAt,
    Instant lastMessageAt,
    Instant resolvedAt,
    Instant closedAt
) {}

/**
 * DTOs pour les Projets Tricot
 */
public record KnittingProjectResponse(
    Long id,
    Long userId,
    String name,
    String description,
    String yarnColor,
    String yarnType,
    Integer needleSize,
    String difficulty,
    Integer estimatedHours,
    String status,
    Integer progress,
    Instant createdAt
) {}

public record CreateKnittingProjectRequest(
    String name,
    String description,
    String yarnColor,
    String yarnType,
    Integer needleSize,
    String difficulty,
    Integer estimatedHours
) {}

/**
 * DTOs pour les Paiements
 */
public record PaymentResponse(
    Long id,
    Long orderId,
    java.math.BigDecimal amount,
    String method,
    String status,
    String transactionId,
    Instant processedAt
) {}

public record CreatePaymentRequest(
    Long orderId,
    java.math.BigDecimal amount,
    String method
) {}
