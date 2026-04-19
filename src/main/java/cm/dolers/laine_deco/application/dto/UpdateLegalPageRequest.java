package cm.dolers.laine_deco.application.dto;
public record UpdateLegalPageRequest(String title, String content, String summary, Boolean isPublished) {
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSummary() { return summary; }
    public Boolean getIsPublished() { return isPublished; }
}
