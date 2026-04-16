package cm.dolers.laine_deco.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscriptionResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String subscribedAt;
    private String unsubscribedAt;
}
