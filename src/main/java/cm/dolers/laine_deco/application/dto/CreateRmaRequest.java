package cm.dolers.laine_deco.application.dto;

public record CreateRmaRequest(
    Long orderId,
    String reason
) {}
