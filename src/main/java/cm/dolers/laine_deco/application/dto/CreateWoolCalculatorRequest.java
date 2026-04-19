package cm.dolers.laine_deco.application.dto;

public record CreateWoolCalculatorRequest(
    String projectName,
    String yarnWeight,
    Integer needleSize,
    Integer width,
    Integer height
) {}

