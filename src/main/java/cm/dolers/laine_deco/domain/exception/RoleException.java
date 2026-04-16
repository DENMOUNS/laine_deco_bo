package cm.dolers.laine_deco.domain.exception;

public final class RoleException extends ApplicationException {
    
    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RoleException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public RoleException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public RoleException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
