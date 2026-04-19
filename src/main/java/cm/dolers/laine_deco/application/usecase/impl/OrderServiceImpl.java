package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.mapper.OrderMapper;
import cm.dolers.laine_deco.application.usecase.OrderService;
import cm.dolers.laine_deco.application.usecase.CouponService;
import cm.dolers.laine_deco.application.usecase.NotificationService;
import cm.dolers.laine_deco.application.usecase.LoyaltyService;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.domain.exception.OrderException;
import cm.dolers.laine_deco.domain.model.OrderStatus;
import cm.dolers.laine_deco.infrastructure.persistence.entity.*;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderJpaRepository orderRepository;
    // private final OrderDetailJpaRepository OrderDetailJpaRepository;
    private final UserJpaRepository userRepository;
    private final ProductJpaRepository ProductJpaRepository;
    private final OrderMapper orderMapper;
    private final CouponService couponService;
    private final NotificationService notificationService;
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final LoyaltyService loyaltyService;

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        log.info("Creating order for user: {}", userId != null ? userId : "GUEST");

        // Validation
        if (request.items() == null || request.items().isEmpty()) {
            throw new OrderException(ErrorCode.ORDER_EMPTY_CART, "Order must contain items");
        }

        if (request.deliveryInfo() == null) {
            throw new OrderException(ErrorCode.OPERATION_FAILED, "Delivery information is required");
        }

        try {
            // 1. Créer et préparer l'entité Order
            var order = new OrderEntity();
            order.setUser(userId != null ? userRepository.findById(userId)
                    .orElseThrow(() -> new OrderException(ErrorCode.USER_NOT_FOUND, "User ID: " + userId))
                    : null);
            order.setOrderDate(LocalDate.now());
            order.setPaymentMethod(request.paymentMethod() != null ? request.paymentMethod() : "CASH_ON_DELIVERY");
            order.setStatus(OrderStatus.PENDING);

            // Générer le numéro de commande unique
            String orderNumber = generateOrderNumber();
            order.setOrderNumber(orderNumber);

            // Stocker les infos de livraison
            var delivery = request.deliveryInfo();
            order.setDeliveryFirstName(delivery.firstName());
            order.setDeliveryLastName(delivery.lastName());
            order.setDeliveryPhone(delivery.phone());
            order.setDeliveryAddress(delivery.address());
            order.setDeliveryCity(delivery.city());
            order.setDeliveryDistrict(delivery.district());
            order.setDeliveryLatitude(delivery.latitude());
            order.setDeliveryLongitude(delivery.longitude());
            order.setNotes(request.notes());

            // 2. Valider les produits et calculer le subtotal
            BigDecimal subtotal = BigDecimal.ZERO;
            for (var item : request.items()) {
                var product = ProductJpaRepository.findById(item.productId())
                        .orElseThrow(() -> new OrderException(ErrorCode.PRODUCT_NOT_FOUND,
                                "Product ID: " + item.productId()));

                if (product.getStockQuantity() < item.quantity()) {
                    throw new OrderException(ErrorCode.PRODUCT_OUT_OF_STOCK,
                            "Product: " + product.getName() + " (need: " + item.quantity() + ", available: "
                                    + product.getStockQuantity() + ")");
                }

                // Créer le détail de commande avec le prix unitaire
                var detail = new OrderDetailEntity();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setQuantity(item.quantity());
                detail.setPrice(item.unitPrice()); // Store the unit price as provided
                order.getDetails().add(detail);

                // Calculer subtotal
                BigDecimal lineTotal = item.unitPrice().multiply(BigDecimal.valueOf(item.quantity()));
                subtotal = subtotal.add(lineTotal);
            }
            order.setSubtotal(subtotal);

            // 3. Gérer le coupon (uniquement si userId NOT NULL)
            BigDecimal discountAmount = BigDecimal.ZERO;
            if (request.couponCode() != null && !request.couponCode().isEmpty()) {
                if (userId != null) {
                    // Utilisateur authentifié - valider et appliquer le coupon
                    try {
                        CouponResponse coupon = couponService.getCouponByCode(request.couponCode());
                        couponService.validateCoupon(request.couponCode());

                        order.setCouponCode(coupon.code());
                        order.setCouponType(coupon.type());

                        // Appliquer la remise selon le type
                        discountAmount = calculateDiscountAmount(coupon, order);
                        couponService.incrementUsage(request.couponCode());
                    } catch (Exception ex) {
                        log.warn("Coupon validation failed for code: {}, ignoring coupon", request.couponCode());
                        discountAmount = BigDecimal.ZERO;
                    }
                } else {
                    // Utilisateur anonyme - ignorer le coupon
                    log.info("Guest user order - ignoring coupon code: {}", request.couponCode());
                }
            }
            order.setDiscountAmount(discountAmount);

            // 4. Calculer le total avec remise
            BigDecimal totalPrice = subtotal.subtract(discountAmount);
            if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
                totalPrice = BigDecimal.ZERO; // Remise ne peut pas être plus que le total
            }
            order.setTotal(totalPrice);

            // 5. Calculer les points de loyauté (uniquement pour utilisateurs authentifiés)
            Integer loyaltyPoints = 0;
            if (userId != null) {
                // 1 point par euro de total (avant remise)
                loyaltyPoints = subtotal.intValue();
            }
            order.setLoyaltyPointsEarned(loyaltyPoints);

            // 6. Sauvegarder l'ordre
            var saved = orderRepository.save(order);
            log.info("Order created: ID={}, user={}, total={}", saved.getId(), userId, saved.getTotal());

            // Créer le premier enregistrement d'historique de statut (PENDING)
            createStatusHistory(saved, OrderStatus.PENDING, "Order created");

            // 7. Décrémenter le stock des produits
            for (var item : request.items()) {
                var product = ProductJpaRepository.findById(item.productId()).get();
                product.setStockQuantity(product.getStockQuantity() - item.quantity());
                ProductJpaRepository.save(product);
            }

            // 8. Déclencher les notifications
            try {
                triggerOrderNotifications(saved, userId);
            } catch (Exception ex) {
                log.error("Error triggering notifications for order: {}", saved.getId(), ex);
                // Don't throw - order is already created
            }

            return orderMapper.toResponse(saved);
        } catch (OrderException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error creating order", ex);
            throw new OrderException(ErrorCode.OPERATION_FAILED, "Error creating order", ex);
        }
    }

    /**
     * Calcule le montant de remise selon le type de coupon
     */
    private BigDecimal calculateDiscountAmount(CouponResponse coupon, OrderEntity order) {
        switch (coupon.type()) {
            case "ALL_PRODUCTS":
                // Remise en pourcentage sur tous les produits
                if (coupon.discountPercentage() != null && coupon.discountPercentage() > 0) {
                    BigDecimal percentage = BigDecimal.valueOf(coupon.discountPercentage())
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                    return order.getSubtotal().multiply(percentage).setScale(2, RoundingMode.HALF_UP);
                }
                break;

            case "SINGLE_PRODUCT":
                // Remise en pourcentage sur un seul produit
                if (coupon.discountPercentage() != null && coupon.discountPercentage() > 0
                        && coupon.applicableProductId() != null) {
                    BigDecimal productLineTotal = order.getDetails().stream()
                            .filter(d -> d.getProduct().getId().equals(coupon.applicableProductId()))
                            .map(d -> d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal percentage = BigDecimal.valueOf(coupon.discountPercentage())
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                    return productLineTotal.multiply(percentage).setScale(2, RoundingMode.HALF_UP);
                }
                break;

            case "FREE_SHIPPING":
                // Remise fixe pour la livraison (ex: 5€)
                if (coupon.discountAmount() != null) {
                    return coupon.discountAmount();
                }
                break;

            case "FIXED_AMOUNT":
                // Remise d'un montant fixe
                if (coupon.discountAmount() != null) {
                    return coupon.discountAmount();
                }
                break;
        }
        return BigDecimal.ZERO;
    }

    /**
     * Déclenche les notifications pour la nouvelle commande et ajoute les points de
     * loyauté
     */
    private void triggerOrderNotifications(OrderEntity order, Long userId) {
        try {
            // Ajouter les points de loyauté (uniquement pour utilisateurs authentifiés)
            if (userId != null) {
                loyaltyService.addPointsFromOrder(userId, order.getTotal());
                log.info("Loyalty points added for user: {}", userId);
            }

            // Notification au client (si authentifié)
            if (userId != null) {
                var notification = new OrderStatusChangeNotificationRequest(
                        order.getId(),
                        "CREATED",
                        "PENDING",
                        "Your order #" + order.getId() + " has been created and is pending confirmation");
                notificationService.notifyOrderStatusChange(notification);
            }

            // Notification au rôle FINANCE et ADMIN
            var adminNotification = new OrderStatusChangeNotificationRequest(
                    order.getId(),
                    "PENDING",
                    "PENDING",
                    "New order #" + order.getId() + " - " + (userId != null ? "User #" + userId : "Guest user"));
            notificationService.notifyOrderStatusChange(adminNotification);
        } catch (Exception ex) {
            log.error("Error sending order notifications", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND, "ID: " + orderId));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND, "ID: " + orderId));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            order.setStatus(newStatus);
            orderRepository.save(order);

            // Enregistrer l'historique du changement de statut
            createStatusHistory(order, newStatus, "Status updated by system");
            log.info("Order status updated: {} -> {}", orderId, status);
        } catch (IllegalArgumentException ex) {
            throw new OrderException(ErrorCode.ORDER_INVALID_STATUS, "Status: " + status);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND, "ID: " + orderId));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderException(ErrorCode.ORDER_CANNOT_CANCEL, "Cannot cancel " + order.getStatus() + " order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Order cancelled: {}", orderId);
    }

    /**
     * Génère un numéro de commande unique: CMD-YYYY-MM-XXXXX
     */
    private String generateOrderNumber() {
        java.time.YearMonth yearMonth = java.time.YearMonth.now();
        // Obtenir le dernier numéro pour ce mois
        Long countThisMonth = orderRepository.countByOrderDateBetween(
                yearMonth.atDay(1),
                yearMonth.atEndOfMonth());
        String sequenceNumber = String.format("%05d", countThisMonth + 1);
        return "CMD-" + yearMonth + "-" + sequenceNumber;
    }

    /**
     * Crée un enregistrement d'historique pour le changement de statut de la
     * commande
     */
    private void createStatusHistory(OrderEntity order, OrderStatus status, String comment) {
        try {
            OrderStatusHistoryEntity history = new OrderStatusHistoryEntity();
            history.setOrder(order);
            history.setStatus(status);
            history.setComment(comment);
            history.setChangedAt(java.time.Instant.now());
            history.setChangedBy("SYSTEM");
            statusHistoryRepository.save(history);
        } catch (Exception e) {
            log.error("Error creating status history for order: {}", order.getId(), e);
            // Don't throw - main operation should succeed even if history fails
        }
    }
}
