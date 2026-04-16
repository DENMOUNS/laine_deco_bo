package cm.dolers.laine_deco.domain.service;

import cm.dolers.laine_deco.infrastructure.persistence.entity.ClientProfileEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.ClientProfileJpaRepository;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ClientProfileService {
    private final ClientProfileJpaRepository clientProfileRepository;

    public ClientProfileService(ClientProfileJpaRepository clientProfileRepository) {
        this.clientProfileRepository = clientProfileRepository;
    }

    public ClientProfileEntity createClientProfile(UserEntity user) {
        if (clientProfileRepository.findByUserId(user.getId()).isPresent()) {
            return clientProfileRepository.findByUserId(user.getId()).get();
        }
        ClientProfileEntity profile = new ClientProfileEntity();
        profile.setUser(user);
        profile.setCreatedAt(Instant.now());
        profile.setUpdatedAt(Instant.now());
        return clientProfileRepository.save(profile);
    }

    public Optional<ClientProfileEntity> getClientProfile(UserEntity user) {
        return clientProfileRepository.findByUserId(user.getId());
    }

    public Optional<ClientProfileEntity> getClientProfileById(Long profileId) {
        return clientProfileRepository.findById(profileId);
    }

    public ClientProfileEntity updateClientProfile(ClientProfileEntity profile) {
        profile.setUpdatedAt(Instant.now());
        return clientProfileRepository.save(profile);
    }

    public void deleteClientProfile(UserEntity user) {
        clientProfileRepository.deleteByUserId(user.getId());
    }
}
