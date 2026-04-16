package cm.dolers.laine_deco.domain.exception;

/**
 * Exception de base scellée (Sealed Class) - Java 17+
 * Permet uniquement aux sous-classes spécifiées d'hériter
 * 
 * Avantages:
 * - Pattern matching exhaustif dans les switch
 * - Meilleure sécurité d'héritage
 * - Compilateur garantit tous les cas couverts
 */
public sealed abstract class ApplicationException extends RuntimeException
    permits ProductException, ExpenseException, AuthException, OrderException, 
            ReviewException, UserException, RoleException, ValidationException {
    
    private final ErrorCode errorCode;
    private final String details;

    protected ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    protected ApplicationException(ErrorCode errorCode, String details) {
        super(buildMessage(errorCode, details));
        this.errorCode = errorCode;
        this.details = details;
    }

    protected ApplicationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = null;
    }

    protected ApplicationException(ErrorCode errorCode, String details, Throwable cause) {
        super(buildMessage(errorCode, details), cause);
        this.errorCode = errorCode;
        this.details = details;
    }

    private static String buildMessage(ErrorCode errorCode, String details) {
        return errorCode.getMessage() + (details != null ? ": " + details : "");
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }
}
