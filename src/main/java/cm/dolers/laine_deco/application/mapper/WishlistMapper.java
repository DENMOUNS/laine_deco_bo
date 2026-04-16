package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.WishlistEntity;

/**
 * Interface Mapper pour Wishlist
 */
public interface WishlistMapper {
    WishlistItemResponse toResponse(WishlistEntity wishlist);
}
