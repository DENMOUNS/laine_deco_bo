package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import cm.dolers.laine_deco.domain.model.LoyaltyTier;
import cm.dolers.laine_deco.domain.model.ThemePreference;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

    @Column(name = "provider_id")
    private String providerId;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column
    private String phone;

    @Column
    private String avatar;

    @ElementCollection
    @Column(name = "password_change_timestamp")
    private List<Long> passwordHistory = new ArrayList<>();

    @Column
    private Integer points = 0;

    @Enumerated(EnumType.STRING)
    @Column
    private LoyaltyTier loyaltyTier;

    @Column(name = "group_id")
    private Long groupId;

    @Lob
    @Column
    private String internalNotes;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(length = 5)
    private String preferredLanguage = "fr";  // fr, en

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThemePreference preferredTheme = ThemePreference.LIGHT;  // LIGHT par défaut

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<UserRoleEntity> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.toLowerCase().trim();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Long> getPasswordHistory() {
        return passwordHistory;
    }

    public void setPasswordHistory(List<Long> passwordHistory) {
        this.passwordHistory = passwordHistory;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LoyaltyTier getLoyaltyTier() {
        return loyaltyTier;
    }

    public void setLoyaltyTier(LoyaltyTier loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public ThemePreference getPreferredTheme() {
        return preferredTheme;
    }

    public void setPreferredTheme(ThemePreference preferredTheme) {
        this.preferredTheme = preferredTheme;
    }

    public Set<UserRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRoleEntity> roles) {
        this.roles = roles;
    }

    public void addRole(UserRoleEntity role) {
        this.roles.add(role);
        role.setUser(this);
    }

    public void removeRole(UserRoleEntity role) {
        this.roles.remove(role);
        role.setUser(null);
    }
}
