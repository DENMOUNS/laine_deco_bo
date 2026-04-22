package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.WishlistService;
import cm.dolers.laine_deco.infrastructure.config.PaginationConstants;
import cm.dolers.laine_deco.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/wishlist")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ClientWishlistController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientWishlistController.class);
    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<Page<WishlistItemResponse>> getWishlist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = SecurityUtils.getCurrentUserId();
        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        log.info("GET /api/client/wishlist - User: {}, page: {}, size: {}", userId, page, normalizedSize);

        List<WishlistItemResponse> results = wishlistService.getUserWishlist(userId);
        int start = page * normalizedSize;
        int end = Math.min(start + normalizedSize, results.size());
        Page<WishlistItemResponse> pageResult = new PageImpl<>(
                results.subList(start, end),
                org.springframework.data.domain.PageRequest.of(page, normalizedSize),
                results.size());
        return ResponseEntity.ok(pageResult);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishlistItemResponse> addToWishlist(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("POST /api/client/wishlist/{} - User: {}", productId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.addToWishlist(userId, productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("DELETE /api/client/wishlist/{} - User: {}", productId, userId);
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearWishlist() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("DELETE /api/client/wishlist - User: {}", userId);
        wishlistService.clearWishlist(userId);
        return ResponseEntity.noContent().build();
    }
}
