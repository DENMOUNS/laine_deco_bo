package cm.dolers.laine_deco.application.dto;
/**
 * Record DTO pour créer/modifier un profil client
 */
public record CreateClientProfileRequest(
    String phone,
    String address,
    String city,
    String zipCode,
    String company,
    String country
) {
    public String taxId() { return null; }
}


