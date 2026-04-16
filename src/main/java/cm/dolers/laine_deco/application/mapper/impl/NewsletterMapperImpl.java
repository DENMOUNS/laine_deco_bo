package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.infrastructure.persistence.entity.NewsletterSubscriptionEntity;
import cm.dolers.laine_deco.application.dto.NewsletterSubscriptionResponse;
import cm.dolers.laine_deco.application.mapper.NewsletterMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsletterMapperImpl implements NewsletterMapper {
    @Override
    public NewsletterSubscriptionResponse toResponse(NewsletterSubscriptionEntity entity) {
        NewsletterSubscriptionResponse response = new NewsletterSubscriptionResponse();
        response.setId(entity.getId());
        response.setEmail(entity.getEmail());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setIsActive(entity.getIsActive());
        response.setSubscribedAt(entity.getSubscribedAt().toString());
        if (entity.getUnsubscribedAt() != null) {
            response.setUnsubscribedAt(entity.getUnsubscribedAt().toString());
        }
        return response;
    }

    @Override
    public List<NewsletterSubscriptionResponse> toResponseList(List<NewsletterSubscriptionEntity> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
