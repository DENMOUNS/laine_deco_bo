package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import cm.dolers.laine_deco.domain.model.PromotionType;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entité pour les promotions (ventes flash, soldes, réductions par marque/catégorie)
 */
@Entity
@Table(name = "promotions")
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromotionType type;

    @Column(nullable = false)
    private Instant startDate;

    @Column(nullable = false)
    private Instant endDate;

    @Column(nullable = false)
    private Boolean isActive = true;

    /**
     * Pourcentage de réduction (0-100)
     * Par ex: 20 pour 20% de réduction
     */
    @Column
    private BigDecimal discountPercentage;

    /**
     * Montant fixe de réduction en euros
     * Ou prix réduit spécifique
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal discountAmount;

    /**
     * Pour les réductions par catégorie
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    /**
     * Pour les réductions par marque
     */
    @Column(name = "brand_name")
    private String brand;

    /**
     * Pour les ventes flash sur un produit spécifique
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column
    private Instant updatedAt;

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PromotionType getType() {
        return type;
    }

    public void setType(PromotionType type) {
        this.type = type;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Vérifie si la promotion est actuellement active (date et statut)
     */
    public boolean isCurrentlyActive() {
        if (!isActive) {
            return false;
        }
        Instant now = Instant.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    /**
     * Récupère le temps restant en millisecondes
     */
    public long getTimeRemainingMs() {
        Instant now = Instant.now();
        if (now.isAfter(endDate)) {
            return 0;
        }
        return endDate.toEpochMilli() - now.toEpochMilli();
    }

    /**
     * Convertit le temps restant en format lisible (jours, heures, minutes)
     */
    public String getTimeRemainingFormatted() {
        long ms = getTimeRemainingMs();
        if (ms <= 0) {
            return "Expiré";
        }

        long seconds = ms / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + "j " + (hours % 24) + "h";
        } else if (hours > 0) {
            return hours + "h " + (minutes % 60) + "m";
        } else if (minutes > 0) {
            return minutes + "m " + (seconds % 60) + "s";
        } else {
            return seconds + "s";
        }
    }
}
