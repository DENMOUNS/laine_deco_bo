package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * SiteConfigEntity
 *
 * CORRECTIONS :
 * - Suppression des champs dupliqués (primaryColor, accentColor, logoUrl... sont
 *   tous des configs génériques stockées en clé/valeur)
 * - L'entité est simplifiée en modèle clé/valeur pur
 * - Suppression du doublon entre `primaryColor` etc. et `key`/`value`
 *
 * NOTE : Si tu as besoin des champs spécifiques (primaryColor, logoUrl...),
 * utilise des clés comme "site.primaryColor", "site.logoUrl" dans la table,
 * et lis-les via SiteConfigService.getConfigValue("site.primaryColor")
 */
@Entity
@Table(name = "site_config")
public class SiteConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_key", nullable = false, unique = true, length = 200)
    private String key;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    // =================== GETTERS / SETTERS ===================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
