package cm.dolers.laine_deco.interfaces.rest.exception;

import cm.dolers.laine_deco.domain.exception.ApplicationException;
import cm.dolers.laine_deco.domain.exception.AuthException;
import cm.dolers.laine_deco.domain.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationException(
            ApplicationException ex, WebRequest request) {
        
        ErrorCode errorCode = ex.getErrorCode();
        String traceId = UUID.randomUUID().toString();
        
        logger.warn("ApplicationException [{}] - Code: {}, Details: {}", 
            traceId, errorCode.getCode(), ex.getDetails());
        
        ApiErrorResponse response = new ApiErrorResponse(
            errorCode.getHttpStatus(),
            errorCode.getCode(),
            errorCode.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(response);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthException(
            AuthException ex, WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        logger.warn("AuthException [{}] - Message: {}", traceId, ex.getMessage());
        
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "AUTH_ERROR",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        Map<String, String> fieldErrors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        logger.info("Validation failed [{}] - Fields: {}", traceId, fieldErrors.size());
        
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            "Erreur de validation des données",
            request.getDescription(false).replace("uri=", "")
        );
        response.setFieldErrors(fieldErrors);
        response.setTraceId(traceId);
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        logger.warn("AuthenticationException [{}] - Message: {}", traceId, ex.getMessage());
        
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "AUTHENTICATION_FAILED",
            "Authentification échouée",
            request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            NoHandlerFoundException ex, WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ErrorCode.RESOURCE_NOT_FOUND.getCode(),
            ErrorCode.RESOURCE_NOT_FOUND.getMessage(),
            ex.getRequestURL()
        );
        response.setTraceId(traceId);
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        logger.error("Unexpected exception [{}]", traceId, ex);
        
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ErrorCode.INTERNAL_ERROR.getCode(),
            ErrorCode.INTERNAL_ERROR.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}
