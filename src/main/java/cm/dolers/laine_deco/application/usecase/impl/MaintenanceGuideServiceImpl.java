package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.infrastructure.persistence.repository.MaintenanceGuideRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ProductRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.CategoryRepository;
import cm.dolers.laine_deco.infrastructure.persistence.entity.MaintenanceGuideEntity;
import cm.dolers.laine_deco.application.dto.CreateMaintenanceGuideRequest;
import cm.dolers.laine_deco.application.dto.MaintenanceGuideResponse;
import cm.dolers.laine_deco.application.mapper.MaintenanceGuideMapper;
import cm.dolers.laine_deco.application.usecase.MaintenanceGuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceGuideServiceImpl implements MaintenanceGuideService {
    private final MaintenanceGuideRepository repository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MaintenanceGuideMapper mapper;

    @Override
    public MaintenanceGuideResponse createGuide(CreateMaintenanceGuideRequest request) {
        MaintenanceGuideEntity guide = new MaintenanceGuideEntity();
        guide.setTitle(request.getTitle());
        guide.setContent(request.getContent());
        guide.setInstructions(request.getInstructions());
        guide.setImage(request.getImage());
        guide.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        guide.setCreatedAt(Instant.now());
        guide.setUpdatedAt(Instant.now());
        
        if (request.getCategoryId() != null) {
            guide.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found")));
        }
        if (request.getBrand() != null) {
            guide.setBrand(request.getBrand());
        }
        if (request.getProductId() != null) {
            guide.setProduct(productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));
        }
        
        MaintenanceGuideEntity saved = repository.save(guide);
        return mapper.toResponse(saved);
    }

    @Override
    public MaintenanceGuideResponse updateGuide(Long guideId, CreateMaintenanceGuideRequest request) {
        MaintenanceGuideEntity guide = repository.findById(guideId)
            .orElseThrow(() -> new IllegalArgumentException("Guide not found: " + guideId));
        
        guide.setTitle(request.getTitle());
        guide.setContent(request.getContent());
        guide.setInstructions(request.getInstructions());
        guide.setImage(request.getImage());
        guide.setIsActive(request.getIsActive());
        guide.setUpdatedAt(Instant.now());
        
        if (request.getCategoryId() != null) {
            guide.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found")));
        } else {
            guide.setCategory(null);
        }
        
        if (request.getBrand() != null) {
            guide.setBrand(request.getBrand());
        } else {
            guide.setBrand(null);
        }
        
        if (request.getProductId() != null) {
            guide.setProduct(productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));
        } else {
            guide.setProduct(null);
        }
        
        MaintenanceGuideEntity updated = repository.save(guide);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceGuideResponse getGuideById(Long guideId) {
        MaintenanceGuideEntity guide = repository.findById(guideId)
            .orElseThrow(() -> new IllegalArgumentException("Guide not found: " + guideId));
        return mapper.toResponse(guide);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceGuideResponse> getAllActiveGuides() {
        return mapper.toResponseList(repository.findAllByIsActiveTrueOrderByCreatedAtDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceGuideResponse> getGuidesByCategory(Long categoryId) {
        return mapper.toResponseList(repository.findByCategoryId(categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceGuideResponse> getGuidesByBrand(String brand) {
        return mapper.toResponseList(repository.findByBrand(brand));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceGuideResponse> getGuidesByProduct(Long productId) {
        return mapper.toResponseList(repository.findByProductId(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceGuideResponse> searchGuides(String search) {
        return mapper.toResponseList(repository.searchByTitle(search));
    }

    @Override
    public void activateGuide(Long guideId) {
        MaintenanceGuideEntity guide = repository.findById(guideId)
            .orElseThrow(() -> new IllegalArgumentException("Guide not found: " + guideId));
        guide.setIsActive(true);
        guide.setUpdatedAt(Instant.now());
        repository.save(guide);
    }

    @Override
    public void deactivateGuide(Long guideId) {
        MaintenanceGuideEntity guide = repository.findById(guideId)
            .orElseThrow(() -> new IllegalArgumentException("Guide not found: " + guideId));
        guide.setIsActive(false);
        guide.setUpdatedAt(Instant.now());
        repository.save(guide);
    }

    @Override
    public void deleteGuide(Long guideId) {
        repository.deleteById(guideId);
    }
}
