package com.articoding.model.in;

public class UpdateLevelForm {

    private String title;

    private String description;

    private Boolean publicLevel;

    private Boolean active;

    public UpdateLevelForm() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isPublicLevel() {
        return publicLevel;
    }

    public void setPublicLevel(Boolean publicLevel) {
        this.publicLevel = publicLevel;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
