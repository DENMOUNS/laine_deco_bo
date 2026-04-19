package cm.dolers.laine_deco.application.dto;

public record BlogPostResponse(Long id, String title, String slug, String summary, String content, String authorName, int viewCount, int likeCount, boolean published, java.time.Instant publishedAt, java.time.Instant createdAt) {}
