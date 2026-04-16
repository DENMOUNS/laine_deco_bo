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
}

/**
 * Service pour Community Posts
 */
public interface CommunityPostService {
    CommunityPostResponse createPost(CreateCommunityPostRequest request);
    CommunityPostResponse getPostById(Long postId);
    Page<CommunityPostResponse> getAllPosts(Pageable pageable);
    Page<CommunityPostResponse> getUserPosts(Long userId, Pageable pageable);
    CommunityPostResponse updatePost(Long postId, CreateCommunityPostRequest request);
    void deletePost(Long postId);
    void likePost(Long postId);
    Long getPostLikesCount(Long postId);
    CommunityCommentResponse addComment(Long postId, String comment);
    Page<CommunityCommentResponse> getPostComments(Long postId, Pageable pageable);
}

/**
 * Service pour Invoices
 */
public interface InvoiceService {
    InvoiceResponse generateInvoice(Long orderId);
    InvoiceResponse getInvoiceById(Long invoiceId);
    InvoiceResponse getInvoiceByNumber(String invoiceNumber);
    Page<InvoiceResponse> getOrderInvoices(Long orderId);
    InvoiceResponse updateInvoiceStatus(Long invoiceId, String status);
    void sendInvoiceToEmail(Long invoiceId, String email);
}

/**
 * Service pour Wool Calculator
 */
public interface WoolCalculatorService {
    WoolCalculatorResponse calculateWool(CreateWoolCalculatorRequest request);
    WoolCalculatorResponse saveCalculation(CreateWoolCalculatorRequest request);
    WoolCalculatorResponse getCalculationById(Long calculationId);
    Page<WoolCalculatorResponse> getUserCalculations(Long userId, Pageable pageable);
    void deleteCalculation(Long calculationId);
}

/**
 * Service pour Knitting Patterns
 */
public interface KnittingPatternService {
    KnittingPatternResponse createPattern(CreateKnittingPatternRequest request);
    KnittingPatternResponse getPatternById(Long patternId);
    Page<KnittingPatternResponse> getAllPatterns(Pageable pageable);
    Page<KnittingPatternResponse> searchPatterns(String keyword, Pageable pageable);
    Page<KnittingPatternResponse> getPatternsBySkillLevel(String level, Pageable pageable);
    KnittingPatternResponse updatePattern(Long patternId, CreateKnittingPatternRequest request);
    void deletePattern(Long patternId);
    void incrementDownloadCount(Long patternId);
}

/**
 * Service pour Promo Events
 */
public interface PromoEventService {
    PromoEventResponse createPromoEvent(CreatePromoEventRequest request);
    PromoEventResponse getPromoEventById(Long eventId);
    Page<PromoEventResponse> getAllPromoEvents(Pageable pageable);
    Page<PromoEventResponse> getActivePromoEvents(Pageable pageable);
    PromoEventResponse updatePromoEvent(Long eventId, CreatePromoEventRequest request);
    void deletePromoEvent(Long eventId);
    void activatePromoEvent(Long eventId);
    void deactivatePromoEvent(Long eventId);
}

/**
 * Service pour RMA (Return Merchandise Authorization)
 */
public interface RmaService {
    RmaResponse createRma(CreateRmaRequest request);
    RmaResponse getRmaById(Long rmaId);
    RmaResponse getRmaByNumber(String rmaNumber);
    Page<RmaResponse> getAllRmas(Pageable pageable);
    Page<RmaResponse> getUserRmas(Long userId, Pageable pageable);
    RmaResponse updateRmaStatus(Long rmaId, String status);
    void approveRma(Long rmaId);
    void rejectRma(Long rmaId);
    void resolveRma(Long rmaId);
}

/**
 * Service pour Site Configuration
 */
public interface SiteConfigService {
    SiteConfigResponse getConfigByKey(String key);
    SiteConfigResponse updateConfig(UpdateSiteConfigRequest request);
    Page<SiteConfigResponse> getAllConfigs(Pageable pageable);
    String getConfigValue(String key);
    void setConfigValue(String key, String value);
}
