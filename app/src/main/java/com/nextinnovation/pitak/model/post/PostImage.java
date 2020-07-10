package com.nextinnovation.pitak.model.post;

import java.io.Serializable;

public class PostImage implements Serializable {
    private String url;
    private String path;
    private String name;

    public PostImage(String url, String name, String path) {
        this.url = url;
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgType() {
        return path;
    }

    public void setImgType(String imgType) {
        this.path = imgType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PostImage{" +
                "content='" + url + '\'' +
                ", imgType='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}


