package cm.dolers.laine_deco.infrastructure.security;

import cm.dolers.laine_deco.domain.model.Role;
import org.springframework.security.core.GrantedAuthority;

public class RoleGrantedAuthority implements GrantedAuthority {
    private final Role role;

    public RoleGrantedAuthority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getAuthority();
    }

    public Role getRole() {
        return role;
    }
}
