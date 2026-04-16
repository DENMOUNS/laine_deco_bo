package cm.dolers.laine_deco.infrastructure.security;

import cm.dolers.laine_deco.domain.model.Role;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

public class AuthenticatedUser implements Principal {
    private final Long id;
    private final String email;
    private final String name;
    private final Set<Role> roles;

    public AuthenticatedUser(Long id, String email, String name, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.roles = roles != null ? roles : Set.of();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean hasAnyRole(Role... requiredRoles) {
        for (Role role : requiredRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public Collection<String> getAuthorities() {
        return roles.stream().map(Role::getAuthority).toList();
    }
}
