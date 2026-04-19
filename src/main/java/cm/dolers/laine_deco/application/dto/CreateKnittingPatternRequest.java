package cm.dolers.laine_deco.application.dto;

public record CreateKnittingPatternRequest(
    String name,
    String author,
    String description,
    String skillLevel,
    Integer estimatedHours,
    String yarnType,
    Integer needleSize,
    String url
) {}
