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
public class ErrorAppender extends AppenderBase<ILoggingEvent> implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (ctx == null) return;
        String level = event.getLevel().toString();
        if (!level.equals("ERROR") && !level.equals("WARN")) return;

        ErrorLogStore store;
        try {
            store = ctx.getBean(ErrorLogStore.class);
        } catch (BeansException e) {
            return;
        }

        IThrowableProxy throwable = event.getThrowableProxy();
        String exType = throwable != null ? throwable.getClassName() : null;
        String stack  = throwable != null ? buildStack(throwable) : null;

        store.add(new ErrorEntry(
            level,
            event.getLoggerName(),
            event.getFormattedMessage(),
            exType,
            stack,
            event.getThreadName()
        ));
    }

    private String buildStack(IThrowableProxy t) {
        StringBuilder sb = new StringBuilder(t.getClassName())
            .append(": ").append(t.getMessage()).append("\n");
        for (StackTraceElementProxy el : t.getStackTraceElementProxyArray()) {
            sb.append("  at ").append(el.getSTEAsString()).append("\n");
        }
        if (t.getCause() != null) {
            sb.append("Caused by: ").append(buildStack(t.getCause()));
        }
        return sb.toString();
    }
}