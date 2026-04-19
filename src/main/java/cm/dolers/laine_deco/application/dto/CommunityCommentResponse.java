package cm.dolers.laine_deco.application.dto;


import java.time.Instant;

public record CommunityCommentResponse(
    Long id,
    Long postId,
    Long userId,
    String userName,
    String comment,
    Integer likesCount,
    Instant createdAt
) {}
