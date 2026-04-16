package cm.dolers.laine_deco.domain.exception;

public final class OrderException extends ApplicationException {
    
    public OrderException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public OrderException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public OrderException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
