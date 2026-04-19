package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.NewsletterSubscriptionResponse;
import cm.dolers.laine_deco.application.mapper.NewsletterMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.NewsletterSubscriptionEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NewsletterMapperImpl implements NewsletterMapper {
    @Override
    public NewsletterSubscriptionResponse toResponse(NewsletterSubscriptionEntity entity) {
        NewsletterSubscriptionResponse response = new NewsletterSubscriptionResponse();
        response.setId(entity.getId());
        response.setEmail(entity.getEmail());
        response.setIsActive(entity.getIsActive());
        response.setSubscribedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        response.setUnsubscribedAt(entity.getUnsubscribedAt() != null ? entity.getUnsubscribedAt().toString() : null);
        return response;
    }

    @Override
    public List<NewsletterSubscriptionResponse> toResponseList(List<NewsletterSubscriptionEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
