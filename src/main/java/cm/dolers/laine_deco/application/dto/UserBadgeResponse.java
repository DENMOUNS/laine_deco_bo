package cm.dolers.laine_deco.application.dto;

public record UserBadgeResponse(Long id, Long userId, Long badgeId, String badgeName, java.time.Instant awardedAt) {}
