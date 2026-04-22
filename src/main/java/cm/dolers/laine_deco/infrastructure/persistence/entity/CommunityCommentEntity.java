package cm.dolers.laine_deco.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * CommunityCommentEntity
 *
 * CORRECTIONS :
 * - Suppression des champs dupliqués `authorId`, `authorName`, `content`
 *   qui faisaient doublon avec `user` (relation) et `comment`
 * - Un seul champ pour le texte du commentaire : `comment`
 * - Un seul champ pour l'auteur : relation `user` (ManyToOne)
 * - Suppression de `@Column(name = "comment_text")` qui créait une 2e colonne
 */
@Entity
@Table(name = "community_comments")
public class CommunityCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_community_comments_post_id"))
    private CommunityPostEntity post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_community_comments_user_id"))
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "likes_count", nullable = false)
    private int likesCount = 0;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // =================== GETTERS / SETTERS ===================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CommunityPostEntity getPost() { return post; }
    public void setPost(CommunityPostEntity post) { this.post = post; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
