package cm.dolers.laine_deco.domain.exception;

public final class UserException extends ApplicationException {
    
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public UserException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public UserException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
