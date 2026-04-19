package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ClientProfileMapper;
import cm.dolers.laine_deco.application.usecase.ClientProfileService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.UserException;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ClientProfileEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ClientProfileJpaRepository;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class ClientProfileServiceImpl implements ClientProfileService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory
            .getLogger(cm.dolers.laine_deco.application.usecase.impl.ClientProfileServiceImpl.class);
    private final ClientProfileJpaRepository profileRepository;
    private final UserJpaRepository userRepository;
    private final ClientProfileMapper profileMapper;

    @Override
    @Transactional(readOnly = true)
    public ClientProfileResponse getClientProfile(Long userId) {
        return profileRepository.findByUserId(userId)
                .map(profileMapper::toResponse)
                .orElseThrow(() -> {
                    log.warn("Client profile not found for user: {}", userId);
                    return new UserException(ErrorCode.USER_PROFILE_UPDATE_FAILED,
                            "Profile not found for user: " + userId);
                });
    }

    @Override
    @Transactional
    public ClientProfileResponse createOrUpdateProfile(Long userId, CreateClientProfileRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "ID: " + userId));

        var profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    var newProfile = new ClientProfileEntity();
                    newProfile.setUser(user);
                    return newProfile;
                });

        profile.setPhone(request.phone());
        profile.setAddress(request.address());
        profile.setCity(request.city());
        profile.setZipCode(request.zipCode());
        profile.setCompany(request.company());
        profile.setTaxId(request.taxId());

        var saved = profileRepository.save(profile);
        log.info("Client profile saved for user: {}", userId);
        return profileMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteClientProfile(Long userId) {
        profileRepository.findByUserId(userId)
                .ifPresent(profile -> {
                    profileRepository.delete(profile);
                    log.info("Client profile deleted for user: {}", userId);
                });
    }
}
