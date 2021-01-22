package org.example.web.dto;

import javax.validation.constraints.NotNull;

public class BookPattern {

    @NotNull
    private String authorPattern;
    @NotNull
    private String titlePattern;
    @NotNull
    private String sizePattern;

    public String getAuthorPattern() {
        return authorPattern;
    }

    public void setAuthorPattern(String authorPattern) {
        this.authorPattern = authorPattern;
    }

    public String getTitlePattern() {
        return titlePattern;
    }

    public void setTitlePattern(String titlePattern) {
        this.titlePattern = titlePattern;
    }

    public String getSizePattern() {
        return sizePattern;
    }

    public void setSizePattern(String sizePattern) {
        this.sizePattern = sizePattern;
    }
}
