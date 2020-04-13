package com.nextinnovation.pitak.model.user;

public class ProfileRequest {
    private String content;

    public ProfileRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
