package cm.dolers.laine_deco.infrastructure.persistence.entity;

import cm.dolers.laine_deco.domain.model.LegalPageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "legal_pages", uniqueConstraints = {
    @UniqueConstraint(columnNames = "type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalPageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private LegalPageType type;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(columnDefinition = "LONGTEXT")
    private String summary;

    @Column(nullable = false)
    private Boolean isPublished = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @Column
    private Instant publishedAt;

    @Column(length = 255)
    private String version;
}
