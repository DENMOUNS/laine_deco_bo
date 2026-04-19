package cm.dolers.laine_deco.application.dto;

public class LegalPageResponse {
    private Long id;
    private String type;
    private String title;
    private String content;
    private String summary;
    private Boolean isPublished;
    private Integer version;
    private String createdAt;
    private String updatedAt;
    private String publishedAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getType() { return type; } public void setType(String type) { this.type = type; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }
    public String getSummary() { return summary; } public void setSummary(String summary) { this.summary = summary; }
    public Boolean getIsPublished() { return isPublished; } public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
    public Integer getVersion() { return version; } public void setVersion(Integer version) { this.version = version; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getPublishedAt() { return publishedAt; } public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }
}
