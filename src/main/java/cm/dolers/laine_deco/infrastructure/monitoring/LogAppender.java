package cm.dolers.laine_deco.infrastructure.monitoring;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * LogAppender — capture TOUS les niveaux de log pour le dashboard de monitoring.
 * 
 * CORRECTION : L'ancienne version filtrait uniquement ERROR/WARN.
 * Désormais on capture INFO, DEBUG, SQL, SECURITY, HTTP également.
 * Les erreurs HTTP 401/403/500 de Spring Security sont loggées en DEBUG
 * par org.springframework.security.* → on les capture maintenant.
 */
@Component
public class LogAppender extends AppenderBase<ILoggingEvent> implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (ctx == null) return;

        // Ignorer les logs trop verbeux pour éviter de saturer le store
        String loggerName = event.getLoggerName();
        if (shouldSkip(loggerName, event.getLevel())) return;

        LogStore store;
        try {
            store = ctx.getBean(LogStore.class);
        } catch (BeansException e) {
            return;
        }

        String level    = event.getLevel().toString();
        String category = detectCategory(level, loggerName, event.getFormattedMessage());

        IThrowableProxy throwable = event.getThrowableProxy();
        String exType = throwable != null ? throwable.getClassName() : null;
        String stack  = throwable != null ? buildStack(throwable) : null;

        store.add(new LogEntry(
                level,
                loggerName,
                category,
                event.getFormattedMessage(),
                exType,
                stack,
                event.getThreadName()
        ));
    }

    /**
     * Décide si on ignore ce log pour ne pas saturer le store.
     * On garde : ERROR, WARN toujours.
     * On garde : DEBUG de Spring Security (contient les 401/403).
     * On ignore : DEBUG trop verbeux d'Hibernate (types, binders).
     */
    private boolean shouldSkip(String loggerName, Level level) {
        // Toujours garder ERROR et WARN
        if (level.isGreaterOrEqual(Level.WARN)) return false;

        // Garder INFO de nos propres classes
        if (level == Level.INFO && loggerName.startsWith("cm.dolers")) return false;

        // Garder DEBUG de Spring Security (401/403 y sont loggés)
        if (level == Level.DEBUG && loggerName.startsWith("org.springframework.security")) return false;

        // Garder DEBUG Hibernate SQL uniquement (pas les type binders)
        if (level == Level.DEBUG && loggerName.equals("org.hibernate.SQL")) return false;

        // Garder DEBUG du DispatcherServlet (routes, erreurs HTTP)
        if (level == Level.DEBUG && loggerName.startsWith("org.springframework.web.servlet.DispatcherServlet")) return false;

        // Garder INFO de Spring Web (mapping, erreurs)
        if (level == Level.INFO && loggerName.startsWith("org.springframework.web")) return false;

        // Ignorer tout le reste au niveau DEBUG/TRACE (trop verbeux)
        if (level.isGreaterOrEqual(Level.INFO)) return false;

        return true; // skip DEBUG/TRACE des autres loggers
    }

    /**
     * Détecte la catégorie d'affichage basée sur le logger et le niveau.
     */
    private String detectCategory(String level, String logger, String message) {
        // Erreurs et warnings ont leur propre catégorie
        if ("ERROR".equals(level)) return "ERROR";
        if ("WARN".equals(level))  return "WARN";

        String l = logger.toLowerCase();
        String m = message != null ? message.toLowerCase() : "";

        // SQL / Hibernate
        if (l.contains("hibernate") || l.equals("org.hibernate.sql")
                || m.startsWith("select ") || m.startsWith("insert ") 
                || m.startsWith("update ") || m.startsWith("delete ")
                || m.startsWith("call ")) {
            return "SQL";
        }

        // Sécurité (inclut les 401/403)
        if (l.contains("security") || l.contains("filterchain") 
                || l.contains("authorizationfilter") || l.contains("auth")
                || m.contains("access denied") || m.contains("401") || m.contains("403")
                || m.contains("unauthorized") || m.contains("forbidden")) {
            return "SECURITY";
        }

        // HTTP / DispatcherServlet
        if (l.contains("dispatcherservlet") || l.contains("requestmapping")
                || l.contains("exceptionhandler") || l.contains("handler")
                || m.contains("http") || m.contains("500") || m.contains("request")) {
            return "HTTP";
        }

        // Debug
        if ("DEBUG".equals(level)) return "DEBUG";

        return "INFO";
    }

    private String buildStack(IThrowableProxy t) {
        StringBuilder sb = new StringBuilder(t.getClassName())
                .append(": ").append(t.getMessage()).append("\n");

        StackTraceElementProxy[] elements = t.getStackTraceElementProxyArray();
        if (elements != null) {
            // Limiter à 20 lignes pour éviter les entrées énormes
            int limit = Math.min(elements.length, 20);
            for (int i = 0; i < limit; i++) {
                sb.append("  at ").append(elements[i].getSTEAsString()).append("\n");
            }
            if (elements.length > 20) {
                sb.append("  ... ").append(elements.length - 20).append(" more\n");
            }
        }

        if (t.getCause() != null) {
            sb.append("Caused by: ").append(buildStack(t.getCause()));
        }
        return sb.toString();
    }
}
