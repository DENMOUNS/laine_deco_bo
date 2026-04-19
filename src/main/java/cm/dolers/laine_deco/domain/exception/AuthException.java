package cm.dolers.laine_deco.domain.exception;

public final class AuthException extends ApplicationException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
    public AuthException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }
    public AuthException(String message) {
        super(ErrorCode.AUTH_UNAUTHORIZED, message);
    }
}
