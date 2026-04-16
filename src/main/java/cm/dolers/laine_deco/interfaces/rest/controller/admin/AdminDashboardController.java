package cm.dolers.laine_deco.interfaces.rest.controller.admin;

import cm.dolers.laine_deco.application.dto.*;
import cm.dolers.laine_deco.application.usecase.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Admin Controller pour le Dashboard
 * Endpoints: Stats générales, analyses, santé stocks
 */
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class AdminDashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        log.info("GET /api/admin/dashboard/stats");
        var response = dashboardService.getDashboardStats();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sales-analytics")
    public ResponseEntity<List<SalesAnalyticsResponse>> getSalesAnalytics(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("GET /api/admin/dashboard/sales-analytics from {} to {}", startDate, endDate);
        var response = dashboardService.getSalesAnalytics(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category-revenue")
    public ResponseEntity<List<CategoryRevenueResponse>> getCategoryRevenue() {
        log.info("GET /api/admin/dashboard/category-revenue");
        var response = dashboardService.getCategoryRevenue();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductResponse>> getTopProducts(
            @RequestParam(defaultValue = "10") Integer limit) {
        log.info("GET /api/admin/dashboard/top-products - Limit: {}", limit);
        var response = dashboardService.getTopProducts(limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inventory-health")
    public ResponseEntity<InventoryHealthResponse> getInventoryHealth() {
        log.info("GET /api/admin/dashboard/inventory-health");
        var response = dashboardService.getInventoryHealth();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/today/orders")
    public ResponseEntity<Long> getTodayOrdersCount() {
        log.info("GET /api/admin/dashboard/today/orders");
        var count = dashboardService.getTodayOrdersCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/today/revenue")
    public ResponseEntity<java.math.BigDecimal> getTodayRevenue() {
        log.info("GET /api/admin/dashboard/today/revenue");
        var revenue = dashboardService.getTodayRevenue();
        return ResponseEntity.ok(revenue);
    }
}
