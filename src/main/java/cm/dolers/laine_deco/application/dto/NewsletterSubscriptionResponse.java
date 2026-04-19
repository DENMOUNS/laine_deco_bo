package cm.dolers.laine_deco.application.dto;

public class NewsletterSubscriptionResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String subscribedAt;
    private String unsubscribedAt;

    public NewsletterSubscriptionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(String subscribedAt) { this.subscribedAt = subscribedAt; }
    public String getUnsubscribedAt() { return unsubscribedAt; }
    public void setUnsubscribedAt(String unsubscribedAt) { this.unsubscribedAt = unsubscribedAt; }
}
