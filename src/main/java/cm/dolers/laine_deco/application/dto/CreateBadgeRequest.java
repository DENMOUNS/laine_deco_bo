package cm.dolers.laine_deco.application.dto;

public record CreateBadgeRequest(
    String name,
    String description,
    String icon,
    String criteria
) {}
