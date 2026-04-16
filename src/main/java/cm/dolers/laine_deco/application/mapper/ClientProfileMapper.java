package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ClientProfileEntity;

/**
 * Interface Mapper pour ClientProfile
 */
public interface ClientProfileMapper {
    ClientProfileResponse toResponse(ClientProfileEntity profile);
    
    ClientProfileEntity createFromRequest(CreateClientProfileRequest request);
}
