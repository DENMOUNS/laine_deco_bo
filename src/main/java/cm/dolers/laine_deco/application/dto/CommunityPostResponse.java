// CommunityPostResponse.java
package cm.dolers.laine_deco.application.dto;

import java.time.Instant;

public record CommunityPostResponse(
    Long id,
    Long userId,
    String userName,
    String title,
    String content,
    Integer likesCount,
    Integer commentsCount,
    Integer viewsCount,
    Instant createdAt
) {}
