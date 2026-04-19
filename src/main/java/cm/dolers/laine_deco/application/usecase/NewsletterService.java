package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.NewsletterSubscriptionResponse;
import cm.dolers.laine_deco.application.dto.SubscribeNewsletterRequest;

import java.util.List;

public interface NewsletterService {
    NewsletterSubscriptionResponse subscribe(SubscribeNewsletterRequest request);

    NewsletterSubscriptionResponse unsubscribe(String email);

    NewsletterSubscriptionResponse resubscribe(String email);

    List<NewsletterSubscriptionResponse> getAllSubscriptions();

    List<NewsletterSubscriptionResponse> getActiveSubscriptions();

    List<NewsletterSubscriptionResponse> searchSubscriptions(String search);

    Long getActiveSubscribersCount();

    void deleteSubscription(Long subscriptionId);

    boolean isSubscribed(String email);
}
