package cm.dolers.laine_deco.application.dto;

public record SubscribeNewsletterRequest(
    String email,
    String firstName,
    String lastName,
    Boolean isSubscribed
) {
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Boolean getIsSubscribed() { return isSubscribed; }
}
