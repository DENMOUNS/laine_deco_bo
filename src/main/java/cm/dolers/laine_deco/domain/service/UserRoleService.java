package cm.dolers.laine_deco.domain.service;

import cm.dolers.laine_deco.domain.model.Role;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserEntity;
import cm.dolers.laine_deco.infrastructure.persistence.entity.UserRoleEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.UserRoleJpaRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    private final UserRoleJpaRepository userRoleRepository;

    public UserRoleService(UserRoleJpaRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void assignRole(UserEntity user, Role role) {
        if (userRoleRepository.findByUserIdAndRole(user.getId(), role).isEmpty()) {
            UserRoleEntity userRole = new UserRoleEntity(user, role);
            user.addRole(userRole);
            userRoleRepository.save(userRole);
        }
    }

    public void removeRole(UserEntity user, Role role) {
        userRoleRepository.deleteByUserIdAndRole(user.getId(), role);
        user.getRoles().removeIf(ur -> ur.getRole() == role);
    }

    public void assignRoles(UserEntity user, Set<Role> roles) {
        roles.forEach(role -> assignRole(user, role));
    }

    public void setRoles(UserEntity user, Set<Role> roles) {
        userRoleRepository.deleteByUserId(user.getId());
        user.getRoles().clear();
        assignRoles(user, roles);
    }

    public Set<Role> getUserRoles(UserEntity user) {
        return userRoleRepository.findByUserId(user.getId()).stream()
                .map(UserRoleEntity::getRole)
                .collect(java.util.stream.Collectors.toSet());
    }

    public boolean userHasRole(UserEntity user, Role role) {
        return userRoleRepository.findByUserIdAndRole(user.getId(), role).isPresent();
    }
}
