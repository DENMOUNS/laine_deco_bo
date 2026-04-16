package cm.dolers.laine_deco.domain.exception;

public final class ReviewException extends ApplicationException {
    
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReviewException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public ReviewException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ReviewException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
