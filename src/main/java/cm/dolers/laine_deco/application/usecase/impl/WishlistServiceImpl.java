package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.WishlistMapper;
import cm.dolers.laine_deco.application.usecase.WishlistService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.UserException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.WishlistEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {
    private final WishlistJpaRepository wishlistRepository;
    private final UserJpaRepository userRepository;
    private final ProductJpaRepository productRepository;
    private final WishlistMapper wishlistMapper;

    @Override
    @Transactional
    public WishlistItemResponse addToWishlist(Long userId, Long productId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId));

        var product = productRepository.findById(productId)
            .orElseThrow(() -> new UserException(ErrorCode.PRODUCT_NOT_FOUND, "Product ID: " + productId));

        var existing = wishlistRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            log.warn("Product already in wishlist for user: {}", userId);
            return wishlistMapper.toResponse(existing.get());
        }

        var wishlist = new WishlistEntity();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        var saved = wishlistRepository.save(wishlist);
        log.info("Product added to wishlist for user: {}", userId);
        return wishlistMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishlistItemResponse> getUserWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId)
            .stream()
            .map(wishlistMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepository.findByUserIdAndProductId(userId, productId)
            .ifPresent(wishlist -> {
                wishlistRepository.delete(wishlist);
                log.info("Product removed from wishlist for user: {}", userId);
            });
    }

    @Override
    @Transactional
    public void clearWishlist(Long userId) {
        wishlistRepository.deleteByUserId(userId);
        log.info("Wishlist cleared for user: {}", userId);
    }
}
