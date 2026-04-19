package cm.dolers.laine_deco.application.dto;

public record CategoryResponse(Long id, String name, String slug, int productCount, java.time.Instant createdAt) {}
