package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.WishlistMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.WishlistEntity;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapperImpl implements WishlistMapper {

    @Override
    public WishlistItemResponse toResponse(WishlistEntity wishlist) {
        return new WishlistItemResponse(
            wishlist.getId(),
            wishlist.getProduct().getId(),
            wishlist.getProduct().getName(),
            "", // ProductEntity doesn't have image field - placeholder for future support
            wishlist.getProduct().getSku(),
            wishlist.getCreatedAt()
        );
    }
}
