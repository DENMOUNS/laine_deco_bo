package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.infrastructure.persistence.repository.NewsletterSubscriptionRepository;
import cm.dolers.laine_deco.infrastructure.persistence.entity.NewsletterSubscriptionEntity;
import cm.dolers.laine_deco.application.dto.NewsletterSubscriptionResponse;
import cm.dolers.laine_deco.application.dto.SubscribeNewsletterRequest;
import cm.dolers.laine_deco.application.mapper.NewsletterMapper;
import cm.dolers.laine_deco.application.usecase.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsletterServiceImpl implements NewsletterService {
    private final NewsletterSubscriptionRepository repository;
    private final NewsletterMapper mapper;

    @Override
    public NewsletterSubscriptionResponse subscribe(SubscribeNewsletterRequest request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            var existing = repository.findByEmail(request.getEmail()).get();
            if (existing.getIsActive()) {
                throw new IllegalArgumentException("Email already subscribed");
            }
            existing.setIsActive(true);
            existing.setUnsubscribedAt(null);
            NewsletterSubscriptionEntity updated = repository.save(existing);
            return mapper.toResponse(updated);
        }

        NewsletterSubscriptionEntity subscription = new NewsletterSubscriptionEntity();
        subscription.setEmail(request.getEmail());
        subscription.setFirstName(request.getFirstName());
        subscription.setLastName(request.getLastName());
        subscription.setIsActive(true);
        subscription.setSubscribedAt(Instant.now());

        NewsletterSubscriptionEntity saved = repository.save(subscription);
        return mapper.toResponse(saved);
    }

    @Override
    public NewsletterSubscriptionResponse unsubscribe(String email) {
        NewsletterSubscriptionEntity subscription = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + email));

        subscription.setIsActive(false);
        subscription.setUnsubscribedAt(Instant.now());
        NewsletterSubscriptionEntity updated = repository.save(subscription);
        return mapper.toResponse(updated);
    }

    @Override
    public NewsletterSubscriptionResponse resubscribe(String email) {
        NewsletterSubscriptionEntity subscription = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + email));

        subscription.setIsActive(true);
        subscription.setUnsubscribedAt(null);
        NewsletterSubscriptionEntity updated = repository.save(subscription);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsletterSubscriptionResponse> getAllSubscriptions() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsletterSubscriptionResponse> getActiveSubscriptions() {
        return mapper.toResponseList(repository.findAllByIsActiveTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsletterSubscriptionResponse> searchSubscriptions(String search) {
        return mapper.toResponseList(repository.searchByEmail(search));
    }

    @Override
    @Transactional(readOnly = true)
    public Long getActiveSubscribersCount() {
        return repository.countByIsActiveTrue();
    }

    @Override
    public void deleteSubscription(Long subscriptionId) {
        repository.deleteById(subscriptionId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubscribed(String email) {
        var subscription = repository.findByEmail(email);
        return subscription.isPresent() && subscription.get().getIsActive();
    }
}
