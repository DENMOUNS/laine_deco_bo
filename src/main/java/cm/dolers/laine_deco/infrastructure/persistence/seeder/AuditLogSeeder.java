package cm.dolers.laine_deco.infrastructure.persistence.seeder;

import cm.dolers.laine_deco.infrastructure.persistence.entity.AuditLogEntity;
import cm.dolers.laine_deco.infrastructure.persistence.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Seeder pour initialiser les logs d'audit avec des données de test
 */
@Configuration
@RequiredArgsConstructor

public class AuditLogSeeder {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuditLogSeeder.class);
    private final AuditLogRepository auditLogRepository;

    @Bean
    public CommandLineRunner seedAuditLogs() {
        return args -> {
            // Vérifier si des logs existent déjà
            if (auditLogRepository.count() > 0) {
                log.info("Audit logs already exist, skipping seeding");
                return;
            }

            log.info("Seeding audit logs...");
            List<AuditLogEntity> auditLogs = generateSampleAuditLogs();
            auditLogRepository.saveAll(auditLogs);
            log.info("Seeded {} audit logs", auditLogs.size());
        };
    }

    private List<AuditLogEntity> generateSampleAuditLogs() {
        List<AuditLogEntity> logs = new ArrayList<>();
        Random random = new Random();

        String[] actions = { "CREATE", "READ", "UPDATE", "DELETE", "LOGIN", "LOGOUT", "EXPORT", "IMPORT" };
        String[] entityTypes = { "Product", "Order", "User", "Category", "Coupon", "Notification", "Review" };
        String[] statuses = { "SUCCESS", "FAILURE", "PARTIAL" };
        String[] ips = { "192.168.1.100", "192.168.1.101", "10.0.0.1", "172.16.0.1", "203.0.113.45", "198.51.100.10" };
        String[] userEmails = { "admin@laine-deco.com", "user1@example.com", "user2@example.com", "guest@example.com" };
        String[] userAgents = {
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36",
                "Mozilla/5.0 (Linux; Android 11; SM-G991B) AppleWebKit/537.36",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15"
        };
        String[] httpMethods = { "GET", "POST", "PUT", "DELETE", "PATCH" };
        String[] requestPaths = {
                "/api/admin/products",
                "/api/admin/products/search",
                "/api/client/products/search",
                "/api/client/orders",
                "/api/client/wishlist",
                "/api/public/products",
                "/api/public/products/popular",
                "/api/admin/users",
                "/api/client/loyalty/redeem",
                "/api/auth/login",
                "/api/auth/logout"
        };
        String[] queryStrings = {
                "page=0&pageSize=10",
                "keyword=lampe&page=0",
                "categoryId=5&minPrice=100000",
                null,
                "sort=POPULARITY&page=1"
        };

        // Générer 100 logs sur les 30 derniers jours
        Instant now = Instant.now();
        for (int i = 0; i < 100; i++) {
            AuditLogEntity auditLog = new AuditLogEntity();

            // Données aléatoires
            auditLog.setUserId((long) (random.nextInt(5) + 1));
            auditLog.setUserEmail(userEmails[random.nextInt(userEmails.length)]);
            auditLog.setAction(actions[random.nextInt(actions.length)]);
            auditLog.setEntityType(entityTypes[random.nextInt(entityTypes.length)]);
            auditLog.setEntityId((long) (random.nextInt(100) + 1));
            auditLog.setDescription("Sample audit log #" + (i + 1));
            auditLog.setHttpMethod(httpMethods[random.nextInt(httpMethods.length)]);
            auditLog.setRequestPath(requestPaths[random.nextInt(requestPaths.length)]);
            auditLog.setQueryString(queryStrings[random.nextInt(queryStrings.length)]);
            auditLog.setIpAddress(ips[random.nextInt(ips.length)]);
            auditLog.setUserAgent(userAgents[random.nextInt(userAgents.length)]);
            auditLog.setStatus(statuses[random.nextInt(statuses.length)]);

            // 10% de chance d'être une erreur
            if (random.nextInt(100) < 10) {
                auditLog.setStatus("FAILURE");
                auditLog.setErrorMessage("Sample error message");
            }

            // Données JSON fictives (10% de chance)
            if (random.nextInt(100) < 10) {
                auditLog.setOldData("{\"id\": 1, \"name\": \"Old Product\"}");
                auditLog.setNewData("{\"id\": 1, \"name\": \"Updated Product\"}");
            }

            // Timestamp aléatoire dans les 30 derniers jours
            long daysAgo = random.nextInt(30);
            long hoursAgo = random.nextInt(24);
            auditLog.setTimestamp(now.minus(daysAgo, ChronoUnit.DAYS).minus(hoursAgo, ChronoUnit.HOURS));

            logs.add(auditLog);
        }

        return logs;
    }
}
