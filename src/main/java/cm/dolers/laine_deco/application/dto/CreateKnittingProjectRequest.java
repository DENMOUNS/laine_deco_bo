package cm.dolers.laine_deco.application.dto;

/**
 * DTO pour créer un projet tricot
 */
public record CreateKnittingProjectRequest(
    String name,
    String description,
    String yarnColor,
    String yarnType,
    Integer needleSize,
    String difficulty,
    Integer estimatedHours
) {}
