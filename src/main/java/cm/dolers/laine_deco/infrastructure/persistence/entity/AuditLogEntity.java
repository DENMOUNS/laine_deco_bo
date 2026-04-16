package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entité pour tracer toutes les actions : qui, quoi, quand, où (IP)
 */
@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_user_id", columnList = "user_id"),
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_timestamp", columnList = "timestamp"),
    @Index(name = "idx_audit_entity_type", columnList = "entity_type")
})
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID de l'utilisateur qui a effectué l'action (NULL pour guest)
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * Email/nom de l'utilisateur pour traçabilité même après suppression
     */
    @Column(name = "user_email")
    private String userEmail;

    /**
     * Type d'action : CREATE, READ, UPDATE, DELETE, LOGIN, LOGOUT, EXPORT, etc.
     */
    @Column(name = "action", nullable = false)
    private String action;

    /**
     * Type d'entité affectée (Product, Order, User, etc.)
     */
    @Column(name = "entity_type", nullable = false)
    private String entityType;

    /**
     * ID de l'entité affectée
     */
    @Column(name = "entity_id")
    private Long entityId;

    /**
     * Description textuelle de ce qui a été fait
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Adresse IP du client
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * User-Agent du navigateur/client
     */
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    /**
     * Statut de l'action : SUCCESS, FAILURE, PARTIAL
     */
    @Column(name = "status")
    private String status = "SUCCESS";

    /**
     * Message d'erreur si la requête a échoué
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * Données anciennes (avant modification) en JSON
     */
    @Column(name = "old_data", columnDefinition = "LONGTEXT")
    private String oldData;

    /**
     * Données nouvelles (après modification) en JSON
     */
    @Column(name = "new_data", columnDefinition = "LONGTEXT")
    private String newData;

    /**
     * Méthode HTTP utilisée (GET, POST, PUT, DELETE, PATCH)
     */
    @Column(name = "http_method", length = 10)
    private String httpMethod;

    /**
     * Chemin de la requête (endpoint) - ex: /api/products/search
     */
    @Column(name = "request_path", columnDefinition = "TEXT")
    private String requestPath;

    /**
     * Query string de la requête (paramètres) - ex: ?keyword=lampe&page=0
     */
    @Column(name = "query_string", columnDefinition = "TEXT")
    private String queryString;

    /**
     * Timestamp de l'action
     */
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp = Instant.now();

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
