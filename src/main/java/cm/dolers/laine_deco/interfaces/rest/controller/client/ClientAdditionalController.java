package cm.dolers.laine_deco.interfaces.rest.controller.client;

/**
 * @deprecated Ce fichier a été séparé en contrôleurs individuels pour respecter le principe SRP.
 * 
 * Classes maintenant dans les fichiers séparés:
 * - ClientBadgeControllerSep.java (User badges)
 * - ClientCommunityPostControllerSep.java (Community posts)
 * - ClientWoolCalculatorControllerSep.java (Wool calculations)
 * 
 * Ce fichier peut être supprimé.
 */
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('CLIENT')")
public class ClientBadgeController {
    private final BadgeService badgeService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyBadges(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/badges/me - User: {}", userId);
        var response = badgeService.getUserBadges(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/recent")
    public ResponseEntity<?> getRecentBadges(@RequestParam(defaultValue = "5") int limit,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/badges/me/recent - User: {}", userId);
        var response = badgeService.getRecentBadges(userId, limit);
        return ResponseEntity.ok(response);
    }
}

/**
 * Client Controller pour Community Posts
 */
@RestController
@RequestMapping("/api/client/community-posts")
@RequiredArgsConstructor
class ClientCommunityPostController {
    private final CommunityPostService postService;

    @PostMapping
    public ResponseEntity<CommunityPostResponse> createPost(@Valid @RequestBody CreateCommunityPostRequest request) {
        log.info("POST /api/client/community-posts - Creating");
        var response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> getPost(@PathVariable Long id) {
        log.info("GET /api/client/community-posts/{}", id);
        var response = postService.getPostById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CommunityPostResponse>> getAllPosts(Pageable pageable) {
        log.info("GET /api/client/community-posts");
        var response = postService.getAllPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<CommunityPostResponse>> getMyPosts(Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/community-posts/me - User: {}", userId);
        var response = postService.getUserPosts(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunityPostResponse> updatePost(@PathVariable Long id,
            @Valid @RequestBody CreateCommunityPostRequest request) {
        log.info("PUT /api/client/community-posts/{}", id);
        var response = postService.updatePost(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("DELETE /api/client/community-posts/{}", id);
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        log.info("POST /api/client/community-posts/{}/like", id);
        postService.likePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommunityCommentResponse>> getPostComments(@PathVariable Long id, Pageable pageable) {
        log.info("GET /api/client/community-posts/{}/comments", id);
        var response = postService.getPostComments(id, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommunityCommentResponse> addComment(@PathVariable Long id,
            @RequestParam String comment) {
        log.info("POST /api/client/community-posts/{}/comments", id);
        var response = postService.addComment(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

/**
 * Client Controller pour Wool Calculator
 */
@RestController
@RequestMapping("/api/client/wool-calculator")
@RequiredArgsConstructor
class ClientWoolCalculatorController {
    private final WoolCalculatorService calculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<WoolCalculatorResponse> calculateWool(@Valid @RequestBody CreateWoolCalculatorRequest request) {
        log.info("POST /api/client/wool-calculator/calculate");
        var response = calculatorService.calculateWool(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<WoolCalculatorResponse> saveCalculation(@Valid @RequestBody CreateWoolCalculatorRequest request) {
        log.info("POST /api/client/wool-calculator/save");
        var response = calculatorService.saveCalculation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WoolCalculatorResponse> getCalculation(@PathVariable Long id) {
        log.info("GET /api/client/wool-calculator/{}", id);
        var response = calculatorService.getCalculationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<WoolCalculatorResponse>> getMyCalculations(Pageable pageable,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("GET /api/client/wool-calculator/me - User: {}", userId);
        var response = calculatorService.getUserCalculations(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalculation(@PathVariable Long id) {
        log.info("DELETE /api/client/wool-calculator/{}", id);
        calculatorService.deleteCalculation(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        // TODO: Implémenter correctement
        return 1L;
    }
}
