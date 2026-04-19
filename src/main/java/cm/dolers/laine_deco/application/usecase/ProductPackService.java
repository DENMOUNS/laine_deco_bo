package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.CreateProductPackRequest;
import cm.dolers.laine_deco.application.dto.ProductPackResponse;

import java.util.List;

public interface ProductPackService {
    ProductPackResponse createProductPack(CreateProductPackRequest request);
    ProductPackResponse updateProductPack(Long packId, CreateProductPackRequest request);
    ProductPackResponse getProductPackById(Long packId);
    List<ProductPackResponse> getAllActivePacks();
    List<ProductPackResponse> getAllPacks();
    List<ProductPackResponse> searchPacksByName(String search);
    List<ProductPackResponse> getPacksWithPromotions();
    List<ProductPackResponse> getPacksContainingProduct(Long productId);
    void deactivateProductPack(Long packId);
    void activateProductPack(Long packId);
    void deleteProductPack(Long packId);
    ProductPackResponse applyPromotionToPack(Long packId, Integer discountPercentage);
    ProductPackResponse removePromotionFromPack(Long packId);
}

