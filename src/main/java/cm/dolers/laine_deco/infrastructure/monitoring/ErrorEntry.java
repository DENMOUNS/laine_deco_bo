package cm.dolers.laine_deco.infrastructure.monitoring;

import java.time.LocalDateTime;

public class ErrorEntry {
    private final LocalDateTime timestamp;
    private final String level;
    private final String loggerName;
    private final String message;
    private final String exceptionType;
    private final String stackTrace;
    private final String threadName;

    public ErrorEntry(String level, String loggerName, String message,
                      String exceptionType, String stackTrace, String threadName) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.loggerName = loggerName;
        this.message = message;
        this.exceptionType = exceptionType;
        this.stackTrace = stackTrace;
        this.threadName = threadName;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getLevel()            { return level; }
    public String getLoggerName()       { return loggerName; }
    public String getMessage()          { return message; }
    public String getExceptionType()    { return exceptionType; }
    public String getStackTrace()       { return stackTrace; }
    public String getThreadName()       { return threadName; }
}