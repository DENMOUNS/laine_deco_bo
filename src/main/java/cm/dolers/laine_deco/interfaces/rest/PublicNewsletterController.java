package cm.dolers.laine_deco.interfaces.rest;

import cm.dolers.laine_deco.application.dto.NewsletterSubscriptionResponse;
import cm.dolers.laine_deco.application.dto.SubscribeNewsletterRequest;
import cm.dolers.laine_deco.application.usecase.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/newsletter")
@RequiredArgsConstructor
public class PublicNewsletterController {
    private final NewsletterService newsletterService;

    @PostMapping("/subscribe")
    public ResponseEntity<NewsletterSubscriptionResponse> subscribe(@RequestBody SubscribeNewsletterRequest request) {
        NewsletterSubscriptionResponse response = newsletterService.subscribe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/unsubscribe/{email}")
    public ResponseEntity<NewsletterSubscriptionResponse> unsubscribe(@PathVariable String email) {
        NewsletterSubscriptionResponse response = newsletterService.unsubscribe(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resubscribe/{email}")
    public ResponseEntity<NewsletterSubscriptionResponse> resubscribe(@PathVariable String email) {
        NewsletterSubscriptionResponse response = newsletterService.resubscribe(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check/{email}")
    public ResponseEntity<Boolean> isSubscribed(@PathVariable String email) {
        boolean isSubscribed = newsletterService.isSubscribed(email);
        return ResponseEntity.ok(isSubscribed);
    }
}
