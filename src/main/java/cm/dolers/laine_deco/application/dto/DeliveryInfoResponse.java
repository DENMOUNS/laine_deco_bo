package cm.dolers.laine_deco.application.dto;

/**
 * Informations de livraison complètes
 */
public record DeliveryInfoResponse(
    String firstName,
    String lastName,
    String phone,
    String address,
    String city,
    String district,
    Double latitude,
    Double longitude
) {}
