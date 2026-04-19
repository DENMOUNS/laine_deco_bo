package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service pour Badges/Achievements
 */
public interface BadgeService {
    BadgeResponse createBadge(CreateBadgeRequest request);
    BadgeResponse getBadgeById(Long badgeId);
    Page<BadgeResponse> getAllBadges(Pageable pageable);
    void awardBadgeToUser(Long userId, Long badgeId);
    List<UserBadgeResponse> getUserBadges(Long userId);
    List<UserBadgeResponse> getRecentBadges(Long userId, int limit);

    void checkAndAwardAutomatic(Long userId);

    org.springframework.data.domain.Page<cm.dolers.laine_deco.application.dto.UserBadgeResponse> getUserBadgesPaginated(Long userId, org.springframework.data.domain.Pageable pageable);

    cm.dolers.laine_deco.application.dto.UserBadgeResponse awardBadge(Long userId, cm.dolers.laine_deco.domain.model.BadgeType badgeType, String reason);

    boolean hasBadge(Long userId, cm.dolers.laine_deco.domain.model.BadgeType badgeType);
}







