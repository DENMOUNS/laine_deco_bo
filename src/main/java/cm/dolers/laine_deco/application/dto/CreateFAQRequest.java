package cm.dolers.laine_deco.application.dto;
public record CreateFAQRequest(String question, String answer, Integer displayOrder, Boolean isActive) {
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public Integer getDisplayOrder() { return displayOrder; }
    public Boolean getIsActive() { return isActive; }
}
