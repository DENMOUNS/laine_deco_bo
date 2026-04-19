package cm.dolers.laine_deco.infrastructure.monitoring;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
        LogStore store;
        try {
            store = ctx.getBean(LogStore.class);
        } catch (BeansException e) {
            return;
        }

        String level    = event.getLevel().toString();
        String logger   = event.getLoggerName();
        String category = detectCategory(level, logger, event.getFormattedMessage());

        IThrowableProxy throwable = event.getThrowableProxy();
        String exType = throwable != null ? throwable.getClassName() : null;
        String stack  = throwable != null ? buildStack(throwable) : null;

        store.add(new LogEntry(level, logger, category,
                event.getFormattedMessage(), exType, stack, event.getThreadName()));
    }

    private String detectCategory(String level, String logger, String message) {
        if (level.equals("ERROR") || level.equals("WARN")) return level;
        String l = logger.toLowerCase();
        String m = message.toLowerCase();
        if (l.contains("hibernate") || l.contains("sql") || m.startsWith("select")
                || m.startsWith("insert") || m.startsWith("update") || m.startsWith("delete"))
            return "SQL";
        if (l.contains("security") || l.contains("filterchain") || l.contains("auth"))
            return "SECURITY";
        if (l.contains("dispatcherservlet") || l.contains("requestmapping")
                || m.contains("http") || m.matches(".*\\b(get|post|put|delete|patch)\\b.*"))
            return "HTTP";
        if (level.equals("DEBUG"))
            return "DEBUG";
        return "INFO";
    }

    private String buildStack(IThrowableProxy t) {
        StringBuilder sb = new StringBuilder(t.getClassName())
                .append(": ").append(t.getMessage()).append("\n");
        for (StackTraceElementProxy el : t.getStackTraceElementProxyArray())
            sb.append("  at ").append(el.getSTEAsString()).append("\n");
        if (t.getCause() != null)
            sb.append("Caused by: ").append(buildStack(t.getCause()));
        return sb.toString();
    }
}