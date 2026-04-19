package cm.dolers.laine_deco.application.dto;

public record KnittingPatternResponse(Long id, String name, String slug, String description, java.math.BigDecimal price, String skillLevel, String format, java.util.List<String> tags, Boolean isActive, java.time.Instant createdAt) {}
