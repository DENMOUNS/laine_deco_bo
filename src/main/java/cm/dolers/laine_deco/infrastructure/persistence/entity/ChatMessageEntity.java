package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Relation vers l'utilisateur (sender)
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "sender_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_chat_messages_sender_id")
    )
    private UserEntity sender;

    // 🔹 Relation vers la conversation (IMPORTANT)
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "conversation_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_chat_messages_conversation_id")
    )
    private ConversationEntity conversation;

    // 🔹 Contenu du message
    @Lob
    @Column(nullable = false)
    private String message;

    // 🔹 Flags
    @Column(name = "is_admin")
    private Boolean isAdmin = false;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    // 🔹 Type de sender (CLIENT / ADMIN)
    @Column(name = "sender_type", nullable = false)
    private String senderType;

    // 🔹 Dates
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "read_at")
    private Instant readAt;

    // 🔹 Champs NON persistés (DTO helpers)
    @Transient
    private Long senderId;

    @Transient
    private String senderName;

    // ================= GETTERS / SETTERS =================

    public Long getId() {
        return id;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}