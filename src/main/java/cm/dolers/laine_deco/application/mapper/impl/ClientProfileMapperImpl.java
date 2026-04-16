package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.ClientProfileMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.ClientProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientProfileMapperImpl implements ClientProfileMapper {

    @Override
    public ClientProfileResponse toResponse(ClientProfileEntity profile) {
        return new ClientProfileResponse(
            profile.getId(),
            profile.getUser().getId(),
            profile.getPhoneNumber(),
            profile.getAddress(),
            profile.getCity(),
            profile.getPostalCode(),
            profile.getCompanyName(),
            profile.getCountry()
        );
    }

    @Override
    public ClientProfileEntity createFromRequest(CreateClientProfileRequest request) {
        var profile = new ClientProfileEntity();
        profile.setPhoneNumber(request.phone());
        profile.setAddress(request.address());
        profile.setCity(request.city());
        profile.setPostalCode(request.zipCode());
        profile.setCompanyName(request.company());
        profile.setCountry(request.country() != null ? request.country() : "");
        return profile;
    }
}
