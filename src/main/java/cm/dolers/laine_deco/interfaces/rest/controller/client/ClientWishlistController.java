package cm.dolers.laine_deco.interfaces.rest.controller.client;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.WishlistService;
import cm.dolers.laine_deco.infrastructure.config.PaginationConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Client Controller pour la gestion de la wishlist
 */
@RestController
@RequestMapping("/api/client/wishlist")
@RequiredArgsConstructor

@PreAuthorize("hasRole('CLIENT')")
public class ClientWishlistController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientWishlistController.class);
    private final WishlistService wishlistService;

    /**
     * Récupérer ma wishlist
     */
    @GetMapping
    public ResponseEntity<Page<WishlistItemResponse>> getWishlist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);

        int normalizedSize = PaginationConstants.normalizePageSize(pageSize);
        log.info("GET /api/client/wishlist - User: {}, page: {}, size: {}", userId, page, normalizedSize);

        List<WishlistItemResponse> results = wishlistService.getUserWishlist(userId);

        // Appliquer la pagination manuellement
        int start = page * normalizedSize;
        int end = Math.min(start + normalizedSize, results.size());
        List<WishlistItemResponse> pageContent = results.subList(start, end);

        Page<WishlistItemResponse> pageResult = new PageImpl<>(
                pageContent,
                org.springframework.data.domain.PageRequest.of(page, normalizedSize),
                results.size());

        return ResponseEntity.ok(pageResult);
    }

    /**
     * Ajouter un produit à la wishlist
     */
    @PostMapping("/{productId}")
    public ResponseEntity<WishlistItemResponse> addToWishlist(
            @PathVariable Long productId,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("POST /api/client/wishlist/{} - User: {}", productId, userId);
        var response = wishlistService.addToWishlist(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retirer un produit de la wishlist
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long productId,
            HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("DELETE /api/client/wishlist/{} - User: {}", productId, userId);
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vider ma wishlist
     */
    @DeleteMapping
    public ResponseEntity<Void> clearWishlist(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        log.info("DELETE /api/client/wishlist - User: {}", userId);
        wishlistService.clearWishlist(userId);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof cm.dolers.laine_deco.infrastructure.security.AuthenticatedUser user) {
            return user.getId();
        }
        return 1L; // Fallback local
    }
}

