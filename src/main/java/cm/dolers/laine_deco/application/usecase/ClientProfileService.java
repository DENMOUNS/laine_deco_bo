package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;

/**
 * Interface Service pour ClientProfile
 * Gestion du profil client (adresse, coordonnées, info fiscal)
 */
public interface ClientProfileService {
    ClientProfileResponse getClientProfile(Long userId);
    
    ClientProfileResponse createOrUpdateProfile(Long userId, CreateClientProfileRequest request);
    
    void deleteClientProfile(Long userId);
}
