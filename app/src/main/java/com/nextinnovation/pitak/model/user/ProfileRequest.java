package com.nextinnovation.pitak.model.user;

import java.io.Serializable;

public class ProfileRequest implements Serializable {
    private String url;
    private long id;

    public ProfileRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProfileRequest{" +
                "url='" + url + '\'' +
                '}';
    }
}
