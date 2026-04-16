package cm.dolers.laine_deco.domain.model;

public enum Role {
    CLIENT("ROLE_CLIENT"),
    FINANCE("ROLE_FINANCE"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public static Role fromAuthority(String authority) {
        for (Role role : Role.values()) {
            if (role.authority.equals(authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown authority: " + authority);
    }
}
