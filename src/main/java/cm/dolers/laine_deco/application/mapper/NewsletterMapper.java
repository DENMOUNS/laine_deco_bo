package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import java.util.List;

public interface NewsletterMapper {
    NewsletterSubscriptionResponse toResponse(NewsletterSubscriptionEntity entity);
    List<NewsletterSubscriptionResponse> toResponseList(List<NewsletterSubscriptionEntity> entities);
}
