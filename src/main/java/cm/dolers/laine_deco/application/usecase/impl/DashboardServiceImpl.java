package cm.dolers.laine_deco.application.usecase.impl;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.DashboardService;
import cm.dolers.laine_deco.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import cm.dolers.laine_deco.infrastructure.persistence.entity.OrderEntity;

@Service
@RequiredArgsConstructor

public class DashboardServiceImpl implements DashboardService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DashboardServiceImpl.class);
    private final UserJpaRepository userRepository;
    private final OrderJpaRepository orderRepository;
    private final ProductJpaRepository ProductJpaRepository;
    private final ExpenseJpaRepository expenseRepository;
    private final ReviewJpaRepository reviewRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        log.info("Fetching dashboard stats");

        long totalUsers = userRepository.count();
        long totalOrders = orderRepository.count();
        BigDecimal totalRevenue = calculateTotalRevenue();
        BigDecimal totalExpenses = calculateTotalExpenses();
        BigDecimal netProfit = totalRevenue.subtract(totalExpenses);

        double averageOrderValue = totalOrders > 0
                ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP).doubleValue()
                : 0;

        long lowStockProducts = ProductJpaRepository.findLowStockProducts().size();
        long pendingReviews = reviewRepository.countByStatus(
                cm.dolers.laine_deco.domain.model.ReviewStatus.PENDING);
        int totalProducts = (int) ProductJpaRepository.count();

        return new DashboardStatsResponse(
                totalUsers,
                totalOrders,
                totalRevenue,
                totalExpenses,
                netProfit,
                averageOrderValue,
                0.0, // À calculer correctement
                lowStockProducts,
                pendingReviews,
                totalProducts,
                LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesAnalyticsResponse> getSalesAnalytics(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching sales analytics from {} to {}", startDate, endDate);

        List<OrderEntity> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        
        Map<LocalDate, List<OrderEntity>> ordersByDate = orders.stream()
            .collect(java.util.stream.Collectors.groupingBy(OrderEntity::getOrderDate));
            
        return ordersByDate.entrySet().stream()
            .map(entry -> {
                LocalDate date = entry.getKey();
                List<OrderEntity> dailyOrders = entry.getValue();
                
                long orderCount = dailyOrders.size();
                BigDecimal revenue = dailyOrders.stream()
                    .map(OrderEntity::getTotal)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                BigDecimal expenses = BigDecimal.ZERO; 
                BigDecimal profit = revenue.subtract(expenses);
                Double profitMargin = revenue.compareTo(BigDecimal.ZERO) > 0 
                    ? profit.divide(revenue, 4, RoundingMode.HALF_UP).doubleValue() * 100 
                    : 0.0;
                    
                return new SalesAnalyticsResponse(date, orderCount, revenue, expenses, profit, profitMargin);
            })
            .sorted(Comparator.comparing(SalesAnalyticsResponse::period))
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryRevenueResponse> getCategoryRevenue() {
        log.info("Fetching category revenue");

        List<OrderEntity> allOrders = orderRepository.findAll();
        
        List<cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity> allDetails = allOrders.stream()
            .flatMap(order -> order.getDetails().stream())
            .filter(detail -> detail.getProduct() != null && detail.getProduct().getCategory() != null)
            .collect(java.util.stream.Collectors.toList());
            
        BigDecimal totalRevenue = allDetails.stream()
            .map(d -> d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        Map<Long, List<cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity>> detailsByCategory = allDetails.stream()
            .collect(java.util.stream.Collectors.groupingBy(d -> d.getProduct().getCategory().getId()));
            
        return detailsByCategory.entrySet().stream()
            .map(entry -> {
                Long categoryId = entry.getKey();
                List<cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity> catDetails = entry.getValue();
                
                String categoryName = catDetails.get(0).getProduct().getCategory().getName();
                
                long productCount = catDetails.stream()
                    .map(d -> d.getProduct().getId())
                    .distinct()
                    .count();
                    
                int salesCount = catDetails.stream()
                    .mapToInt(cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity::getQuantity)
                    .sum();
                    
                BigDecimal revenue = catDetails.stream()
                    .map(d -> d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                Double percentage = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                    ? revenue.divide(totalRevenue, 4, RoundingMode.HALF_UP).doubleValue() * 100
                    : 0.0;
                    
                return new CategoryRevenueResponse(categoryId, categoryName, productCount, revenue, salesCount, percentage);
            })
            .sorted((c1, c2) -> c2.revenue().compareTo(c1.revenue()))
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopProductResponse> getTopProducts(Integer limit) {
        log.info("Fetching top {} products", limit);

        List<OrderEntity> allOrders = orderRepository.findAll();
        
        List<cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity> allDetails = allOrders.stream()
            .flatMap(order -> order.getDetails().stream())
            .filter(detail -> detail.getProduct() != null)
            .collect(java.util.stream.Collectors.toList());
            
        Map<Long, List<cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity>> detailsByProduct = allDetails.stream()
            .collect(java.util.stream.Collectors.groupingBy(d -> d.getProduct().getId()));
            
        return detailsByProduct.entrySet().stream()
            .map(entry -> {
                Long productId = entry.getKey();
                List<cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity> prodDetails = entry.getValue();
                
                cm.dolers.laine_deco.infrastructure.persistence.entity.ProductEntity product = prodDetails.get(0).getProduct();
                
                int salesCount = prodDetails.stream()
                    .mapToInt(cm.dolers.laine_deco.infrastructure.persistence.entity.OrderDetailEntity::getQuantity)
                    .sum();
                    
                BigDecimal revenue = prodDetails.stream()
                    .map(d -> d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                Double rating = product.getRating() != null ? product.getRating().doubleValue() : 0.0;
                
                return new TopProductResponse(productId, product.getName(), product.getSku(), salesCount, revenue, rating);
            })
            .sorted((p1, p2) -> Integer.compare(p2.salesCount(), p1.salesCount()))
            .limit(limit)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryHealthResponse getInventoryHealth() {
        log.info("Fetching inventory health");

        long totalProducts = ProductJpaRepository.count();
        long lowStockProducts = ProductJpaRepository.findLowStockProducts().size();
        long outOfStockProducts = ProductJpaRepository.count() - lowStockProducts; // Simplifié

        return new InventoryHealthResponse(
                totalProducts,
                totalProducts - lowStockProducts - outOfStockProducts,
                lowStockProducts,
                outOfStockProducts,
                BigDecimal.ZERO, // À calculer
                0.0 // À calculer
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTodayOrdersCount() {
        return orderRepository.countByOrderDateBetween(LocalDate.now(), LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTodayRevenue() {
        List<OrderEntity> todayOrders = 
            orderRepository.findByOrderDateBetween(LocalDate.now(), LocalDate.now());
        
        return todayOrders.stream()
                .map(OrderEntity::getTotal)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalRevenue() {
        return orderRepository.findAll().stream()
                .map(OrderEntity::getTotal)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalExpenses() {
        return expenseRepository.findAll().stream()
                .map(cm.dolers.laine_deco.infrastructure.persistence.entity.ExpenseEntity::getAmount)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
