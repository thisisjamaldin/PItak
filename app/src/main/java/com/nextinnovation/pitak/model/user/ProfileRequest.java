package com.nextinnovation.pitak.model.user;

import java.io.Serializable;

public class ProfileRequest implements Serializable {
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

    @Override
    public String toString() {
        return "ProfileRequest{" +
                "content='" + content + '\'' +
                '}';
    }
}
