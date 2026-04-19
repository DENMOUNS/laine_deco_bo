package cm.dolers.laine_deco.infrastructure.config;

import cm.dolers.laine_deco.application.usecase.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Tâche planifiée pour gérer l'expiration des promotions automatiquement
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
public class PromotionScheduledTasks {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PromotionScheduledTasks.class);
    private final PromotionService promotionService;

    /**
     * Vérifie et désactive toutes les promotions expirées
     * S'exécute toutes les heures
     */
    @Scheduled(fixedRate = 3600000)  // 1 heure en millisecondes
    public void deactivateExpiredPromotions() {
        log.info("Running scheduled task: deactivateExpiredPromotions");
        try {
            promotionService.deactivateExpiredPromotions();
            log.info("Scheduled task completed successfully");
        } catch (Exception e) {
            log.error("Error in scheduled task: {}", e.getMessage(), e);
        }
    }
}



