package cm.dolers.laine_deco.interfaces.rest.exception;

import cm.dolers.laine_deco.domain.exception.ApplicationException;
import cm.dolers.laine_deco.domain.exception.AuthException;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import cm.dolers.laine_deco.infrastructure.monitoring.LogAppender;
import cm.dolers.laine_deco.infrastructure.monitoring.LogEntry;
import cm.dolers.laine_deco.infrastructure.monitoring.LogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * GlobalExceptionHandler — intercepte TOUTES les exceptions et :
 * 1. Retourne un JSON standardisé (plus jamais de page HTML d'erreur)
 * 2. Envoie l'erreur au LogStore pour le dashboard de monitoring
 *
 * CORRECTIONS :
 * - Ajout handlers pour AccessDeniedException (403), AuthenticationException (401)
 * - Ajout handlers pour HttpMessageNotReadableException (body JSON malformé)
 * - Ajout handlers pour MissingServletRequestParameterException
 * - Ajout handlers pour MethodArgumentTypeMismatchException
 * - Tous les handlers envoient au LogStore
 */
@RestControllerAdvice(basePackages = "cm.dolers.laine_deco.interfaces.rest")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final LogStore logStore;

    public GlobalExceptionHandler(LogStore logStore) {
        this.logStore = logStore;
    }

    // =================== EXCEPTIONS DOMAINE ===================

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationException(
            ApplicationException ex, WebRequest request) {

        ErrorCode errorCode = ex.getErrorCode();
        String traceId = UUID.randomUUID().toString().substring(0, 8);

        logger.warn("ApplicationException [{}] - Code: {}, Details: {}",
                traceId, errorCode.getCode(), ex.getDetails());

        // Envoyer au dashboard monitoring seulement si >= 500
        if (errorCode.getHttpStatus() >= 500) {
            sendToLogStore("ERROR", ex.getMessage(), ex.getClass().getName(),
                    buildStackString(ex), request);
        }

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(buildError(errorCode.getHttpStatus(), errorCode.getCode(),
                        errorCode.getMessage(), request, traceId));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthException(
            AuthException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        logger.warn("AuthException [{}] - Message: {}", traceId, ex.getMessage());
        sendToLogStore("WARN", "AuthException: " + ex.getMessage(), ex.getClass().getName(),
                null, request);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildError(401, "AUTH_ERROR", ex.getMessage(), request, traceId));
    }

    // =================== EXCEPTIONS SPRING SECURITY ===================

    /**
     * NOUVEAU : Capture les 403 Forbidden de Spring Security (@PreAuthorize)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        logger.warn("AccessDenied [{}] - {}", traceId, ex.getMessage());
        sendToLogStore("WARN", "403 Forbidden: " + ex.getMessage(), 
                "AccessDeniedException", null, request);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildError(403, "ACCESS_DENIED", "Accès refusé — droits insuffisants",
                        request, traceId));
    }

    /**
     * NOUVEAU : Capture les 401 Unauthorized de Spring Security
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        logger.warn("AuthenticationFailed [{}] - {}", traceId, ex.getMessage());
        sendToLogStore("WARN", "401 Unauthorized: " + ex.getMessage(),
                ex.getClass().getName(), null, request);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildError(401, "AUTHENTICATION_FAILED", "Authentification requise",
                        request, traceId));
    }

    // =================== EXCEPTIONS VALIDATION ===================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message   = error.getDefaultMessage();
            fieldErrors.put(fieldName, message);
        });

        logger.info("ValidationFailed [{}] - {} champs invalides", traceId, fieldErrors.size());

        ApiErrorResponse response = buildError(400, "VALIDATION_ERROR",
                "Données invalides — vérifiez les champs", request, traceId);
        response.setFieldErrors(fieldErrors);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * NOUVEAU : Body JSON malformé
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        logger.warn("MalformedJSON [{}] - {}", traceId, ex.getMessage());

        return ResponseEntity.badRequest()
                .body(buildError(400, "INVALID_JSON",
                        "Corps de la requête invalide ou manquant", request, traceId));
    }

    /**
     * NOUVEAU : Paramètre de requête manquant
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        String message = "Paramètre requis manquant: " + ex.getParameterName();

        return ResponseEntity.badRequest()
                .body(buildError(400, "MISSING_PARAMETER", message, request, traceId));
    }

    /**
     * NOUVEAU : Type de paramètre incorrect (ex: String là où Long attendu)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        String message = "Type incorrect pour le paramètre '" + ex.getName() + "'";

        return ResponseEntity.badRequest()
                .body(buildError(400, "TYPE_MISMATCH", message, request, traceId));
    }

    /**
     * NOUVEAU : Méthode HTTP non supportée (ex: POST sur une route GET)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        String message = "Méthode " + ex.getMethod() + " non supportée pour cette route";

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(buildError(405, "METHOD_NOT_ALLOWED", message, request, traceId));
    }

    /**
     * NOUVEAU : Content-Type non supporté
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(buildError(415, "UNSUPPORTED_MEDIA_TYPE",
                        "Content-Type non supporté. Utilisez application/json", request, traceId));
    }

    // =================== EXCEPTIONS GENERIQUES ===================

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            NoHandlerFoundException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        String message = "Route non trouvée: " + ex.getHttpMethod() + " " + ex.getRequestURL();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(404, ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                        message, request, traceId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        logger.warn("IllegalArgument [{}] - {}", traceId, ex.getMessage());

        return ResponseEntity.badRequest()
                .body(buildError(400, "INVALID_ARGUMENT", ex.getMessage(), request, traceId));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        logger.error("UnhandledException [{}] - {}", traceId, ex.getMessage(), ex);

        // Toujours envoyer les 500 au dashboard
        sendToLogStore("ERROR", "500 Internal Server Error: " + ex.getMessage(),
                ex.getClass().getName(), buildStackString(ex), request);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(500, ErrorCode.INTERNAL_ERROR.getCode(),
                        "Erreur interne — contactez le support (ref: " + traceId + ")",
                        request, traceId));
    }

    // =================== HELPERS ===================

    private ApiErrorResponse buildError(int status, String code, String message,
                                         WebRequest request, String traceId) {
        String path = request.getDescription(false).replace("uri=", "");
        ApiErrorResponse response = new ApiErrorResponse(status, code, message, path);
        response.setTraceId(traceId);
        return response;
    }

    private void sendToLogStore(String level, String message, String exType,
                                  String stack, WebRequest request) {
        if (logStore == null) return;
        try {
            String path = request.getDescription(false);
            logStore.add(new LogEntry(
                    level,
                    GlobalExceptionHandler.class.getName(),
                    "HTTP",
                    message + " | " + path,
                    exType,
                    stack,
                    Thread.currentThread().getName()
            ));
        } catch (Exception ignored) {
            // Ne jamais crasher à cause du log
        }
    }

    private String buildStackString(Throwable ex) {
        if (ex == null) return null;
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getClass().getName()).append(": ").append(ex.getMessage()).append("\n");
        StackTraceElement[] elements = ex.getStackTrace();
        int limit = Math.min(elements.length, 15);
        for (int i = 0; i < limit; i++) {
            sb.append("  at ").append(elements[i]).append("\n");
        }
        return sb.toString();
    }
}
