package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.NewsletterSubscriptionResponse;
import cm.dolers.laine_deco.application.usecase.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/newsletter")
@RequiredArgsConstructor
public class AdminNewsletterController {
    private final NewsletterService newsletterService;

    @GetMapping
    public ResponseEntity<List<NewsletterSubscriptionResponse>> getAllSubscriptions() {
        List<NewsletterSubscriptionResponse> subscriptions = newsletterService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/active")
    public ResponseEntity<List<NewsletterSubscriptionResponse>> getActiveSubscriptions() {
        List<NewsletterSubscriptionResponse> subscriptions = newsletterService.getActiveSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NewsletterSubscriptionResponse>> search(@RequestParam String search) {
        List<NewsletterSubscriptionResponse> subscriptions = newsletterService.searchSubscriptions(search);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getActiveCount() {
        Long count = newsletterService.getActiveSubscribersCount();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long subscriptionId) {
        newsletterService.deleteSubscription(subscriptionId);
        return ResponseEntity.noContent().build();
    }
}

