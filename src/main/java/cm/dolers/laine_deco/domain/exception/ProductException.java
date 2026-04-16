package cm.dolers.laine_deco.domain.exception;

/**
 * Exception spécialisée pour les erreurs liées au domaine Produit
 */
public final class ProductException extends ApplicationException {
    
    public ProductException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ProductException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public ProductException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ProductException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
