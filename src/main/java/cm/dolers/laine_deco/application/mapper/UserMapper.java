package cm.dolers.laine_deco.application.mapper;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;

/**
 * Interface Mapper pour User (SOLID - Dependency Inversion)
 */
public interface UserMapper {
    UserResponse toUserResponse(UserEntity user);
    
    UserEntity createUserFromRequest(CreateUserRequest request);
}
