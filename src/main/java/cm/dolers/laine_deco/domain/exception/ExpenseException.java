package cm.dolers.laine_deco.domain.exception;

public final class ExpenseException extends ApplicationException {
    
    public ExpenseException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ExpenseException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public ExpenseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ExpenseException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
