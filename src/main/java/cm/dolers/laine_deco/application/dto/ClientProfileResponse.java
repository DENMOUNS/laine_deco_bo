package cm.dolers.laine_deco.application.dto;

/**
 * Record DTO pour le profil client
 */
public record ClientProfileResponse(
    Long id,
    Long userId,
    String phone,
    String address,
    String city,
    String zipCode,
    String company,
    String country
) {}

