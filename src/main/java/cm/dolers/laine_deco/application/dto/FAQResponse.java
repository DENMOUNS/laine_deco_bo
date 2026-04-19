package cm.dolers.laine_deco.application.dto;

public record FAQResponse(Long id, String question, String answer, Integer displayOrder, Boolean isActive, java.time.Instant createdAt) {}
