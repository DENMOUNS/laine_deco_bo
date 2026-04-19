package cm.dolers.laine_deco.application.dto;

public record CreateLegalPageRequest(
    String title,
    String slug,
    String content,
    String summary,
    String version,
    Boolean isPublished,
    Boolean requiresAcceptance,
    String type
) {
    public String getTitle() { return title; }
    public String getSlug() { return slug; }
    public String getContent() { return content; }
    public String getSummary() { return summary; }
    public String getVersion() { return version; }
    public Boolean getIsPublished() { return isPublished; }
    public Boolean getRequiresAcceptance() { return requiresAcceptance; }
    public String getType() { return type; }
}

