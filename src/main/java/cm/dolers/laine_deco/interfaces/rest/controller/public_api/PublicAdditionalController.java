package cm.dolers.laine_deco.interfaces.rest.controller.public_api;

/**
 * @deprecated Ce fichier a été séparé en contrôleurs individuels pour respecter le principe SRP.
 * 
 * Classes maintenant dans les fichiers séparés:
 * - PublicKnittingPatternControllerSep.java (Knitting patterns)
 * - PublicPromoEventControllerSep.java (Promo events)
 * - PublicBadgeControllerSep.java (Badges)
 * - PublicCommunityPostControllerSep.java (Community posts)
 * 
 * Ce fichier peut être supprimé.
 */

    @GetMapping("/{id}")
    public ResponseEntity<KnittingPatternResponse> getPattern(@PathVariable Long id) {
        log.info("GET /api/public/knitting-patterns/{}", id);
        var response = patternService.getPatternById(id);
        patternService.incrementDownloadCount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<KnittingPatternResponse>> getAllPatterns(Pageable pageable) {
        log.info("GET /api/public/knitting-patterns");
        var response = patternService.getAllPatterns(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<KnittingPatternResponse>> searchPatterns(@RequestParam String keyword, Pageable pageable) {
        log.info("GET /api/public/knitting-patterns/search - Keyword: {}", keyword);
        var response = patternService.searchPatterns(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/skill/{level}")
    public ResponseEntity<Page<KnittingPatternResponse>> getPatternsBySkill(@PathVariable String level, Pageable pageable) {
        log.info("GET /api/public/knitting-patterns/skill/{}", level);
        var response = patternService.getPatternsBySkillLevel(level, pageable);
        return ResponseEntity.ok(response);
    }
}

/**
 * Public Controller pour Promo Events
 */
@RestController
@RequestMapping("/api/public/promo-events")
@RequiredArgsConstructor
class PublicPromoEventController {
    private final PromoEventService promoEventService;

    @GetMapping
    public ResponseEntity<Page<PromoEventResponse>> getAllPromoEvents(Pageable pageable) {
        log.info("GET /api/public/promo-events");
        var response = promoEventService.getAllPromoEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<PromoEventResponse>> getActivePromoEvents(Pageable pageable) {
        log.info("GET /api/public/promo-events/active");
        var response = promoEventService.getActivePromoEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoEventResponse> getPromoEvent(@PathVariable Long id) {
        log.info("GET /api/public/promo-events/{}", id);
        var response = promoEventService.getPromoEventById(id);
        return ResponseEntity.ok(response);
    }
}

/**
 * Public Controller pour Badges
 */
@RestController
@RequestMapping("/api/public/badges")
@RequiredArgsConstructor
class PublicBadgeController {
    private final BadgeService badgeService;

    @GetMapping
    public ResponseEntity<Page<BadgeResponse>> getAllBadges(Pageable pageable) {
        log.info("GET /api/public/badges");
        var response = badgeService.getAllBadges(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeResponse> getBadge(@PathVariable Long id) {
        log.info("GET /api/public/badges/{}", id);
        var response = badgeService.getBadgeById(id);
        return ResponseEntity.ok(response);
    }
}

/**
 * Public Controller pour Community Posts
 */
@RestController
@RequestMapping("/api/public/community-posts")
@RequiredArgsConstructor
class PublicCommunityPostController {
    private final CommunityPostService postService;

    @GetMapping
    public ResponseEntity<Page<CommunityPostResponse>> getAllPosts(Pageable pageable) {
        log.info("GET /api/public/community-posts");
        var response = postService.getAllPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> getPost(@PathVariable Long id) {
        log.info("GET /api/public/community-posts/{}", id);
        var response = postService.getPostById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommunityCommentResponse>> getPostComments(@PathVariable Long id, Pageable pageable) {
        log.info("GET /api/public/community-posts/{}/comments", id);
        var response = postService.getPostComments(id, pageable);
        return ResponseEntity.ok(response);
    }
}
