package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import cm.dolers.laine_deco.domain.model.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderEntity
 *
 * CORRECTIONS :
 * - Suppression du champ `tax` (doublon de `taxAmount`)
 * - `getClient()` / `setClient()` → alias vers `user` pour compatibilité mappers
 * - `orderType` → alias de `type`
 * - Tous les champs nullable correctement annotés
 * - `user` est nullable (commandes guest)
 */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NULLABLE = true pour les commandes guest
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true,
            foreignKey = @ForeignKey(name = "FK_orders_user_id"))
    private UserEntity user;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", orphanRemoval = true,
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetailEntity> details = new ArrayList<>();

    @OneToMany(mappedBy = "order", orphanRemoval = true,
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderStatusHistoryEntity> statusHistory = new ArrayList<>();

    @Column(unique = true, length = 50)
    private String orderNumber;

    @Column
    private String type;

    // CORRECTION : Un seul champ tax (suppression du doublon `tax`)
    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column
    private String address;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "delivery_first_name")
    private String deliveryFirstName;

    @Column(name = "delivery_last_name")
    private String deliveryLastName;

    @Column(name = "delivery_phone")
    private String deliveryPhone;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_city")
    private String deliveryCity;

    @Column(name = "delivery_district")
    private String deliveryDistrict;

    @Column(name = "delivery_latitude")
    private Double deliveryLatitude;

    @Column(name = "delivery_longitude")
    private Double deliveryLongitude;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "coupon_type")
    private String couponType;

    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "subtotal", precision = 19, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "loyalty_points_earned")
    private Integer loyaltyPointsEarned = 0;

    @Lob
    @Column
    private String notes;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    // =================== GETTERS / SETTERS ===================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    /** Alias pour les mappers qui utilisent getClient() */
    public UserEntity getClient() { return user; }
    public void setClient(UserEntity client) { this.user = client; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public List<OrderDetailEntity> getDetails() { return details; }
    public void setDetails(List<OrderDetailEntity> details) { this.details = details; }

    public List<OrderStatusHistoryEntity> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<OrderStatusHistoryEntity> statusHistory) { this.statusHistory = statusHistory; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public void setOrderType(String type) { this.type = type; }

    /** CORRECTION : Un seul getter/setter pour la taxe */
    public BigDecimal getTaxAmount() { return taxAmount != null ? taxAmount : BigDecimal.ZERO; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    /** Alias utilisé dans OrderMapperImpl */
    public BigDecimal getTax() { return getTaxAmount(); }
    public void setTax(BigDecimal tax) { this.taxAmount = tax; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getDeliveryFirstName() { return deliveryFirstName; }
    public void setDeliveryFirstName(String v) { this.deliveryFirstName = v; }

    public String getDeliveryLastName() { return deliveryLastName; }
    public void setDeliveryLastName(String v) { this.deliveryLastName = v; }

    public String getDeliveryPhone() { return deliveryPhone; }
    public void setDeliveryPhone(String v) { this.deliveryPhone = v; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String v) { this.deliveryAddress = v; }

    public String getDeliveryCity() { return deliveryCity; }
    public void setDeliveryCity(String v) { this.deliveryCity = v; }

    public String getDeliveryDistrict() { return deliveryDistrict; }
    public void setDeliveryDistrict(String v) { this.deliveryDistrict = v; }

    public Double getDeliveryLatitude() { return deliveryLatitude; }
    public void setDeliveryLatitude(Double v) { this.deliveryLatitude = v; }

    public Double getDeliveryLongitude() { return deliveryLongitude; }
    public void setDeliveryLongitude(Double v) { this.deliveryLongitude = v; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public String getCouponType() { return couponType; }
    public void setCouponType(String couponType) { this.couponType = couponType; }

    public BigDecimal getDiscountAmount() { return discountAmount != null ? discountAmount : BigDecimal.ZERO; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getSubtotal() { return subtotal != null ? subtotal : BigDecimal.ZERO; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Integer getLoyaltyPointsEarned() { return loyaltyPointsEarned; }
    public void setLoyaltyPointsEarned(Integer v) { this.loyaltyPointsEarned = v; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    public void preUpdate() { this.updatedAt = Instant.now(); }
}
