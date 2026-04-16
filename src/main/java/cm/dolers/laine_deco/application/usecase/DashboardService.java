package cm.dolers.laine_deco.application.usecase;

import cm.dolers.laine_deco.application.dto.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface Service pour Dashboard & Analytics
 * Fournit les statistiques pour le tableau de bord administrateur
 */
public interface DashboardService {
    /**
     * Récupérer les statistiques générales du dashboard
     */
    DashboardStatsResponse getDashboardStats();
    
    /**
     * Analysts des ventes par période (Jour, Semaine, Mois)
     */
    List<SalesAnalyticsResponse> getSalesAnalytics(LocalDate startDate, LocalDate endDate);
    
    /**
     * Revenus par catégorie de produit
     */
    List<CategoryRevenueResponse> getCategoryRevenue();
    
    /**
     * Top 10 des produits les plus vendus
     */
    List<TopProductResponse> getTopProducts(Integer limit);
    
    /**
     * État de la santé des stocks
     */
    InventoryHealthResponse getInventoryHealth();
    
    /**
     * Commandes du jour
     */
    Long getTodayOrdersCount();
    
    /**
     * Revenue du jour
     */
    java.math.BigDecimal getTodayRevenue();
}
