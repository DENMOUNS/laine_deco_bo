package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import java.util.List;

/**
 * Interface Service pour Wishlist
 * Gestion de la liste de souhaits du client
 */
public interface WishlistService {
    WishlistItemResponse addToWishlist(Long userId, Long productId);
    
    List<WishlistItemResponse> getUserWishlist(Long userId);
    
    void removeFromWishlist(Long userId, Long productId);
    
    void clearWishlist(Long userId);
}
