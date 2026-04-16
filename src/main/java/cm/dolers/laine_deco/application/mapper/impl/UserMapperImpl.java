package cm.dolers.laine_deco.application.mapper.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.UserMapper;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse toUserResponse(UserEntity user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getPhone(),
            user.getLoyaltyTier() != null ? user.getLoyaltyTier().toString() : "BRONZE",
            user.getPoints() != null ? user.getPoints() : 0,
            user.getPreferredLanguage() != null ? user.getPreferredLanguage() : "fr",
            user.getPreferredTheme() != null ? user.getPreferredTheme().getValue() : "light",
            user.getCreatedAt()
        );
    }

    @Override
    public UserEntity createUserFromRequest(CreateUserRequest request) {
        var user = new UserEntity();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPhone(request.phone());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setPoints(0);
        return user;
    }
}
