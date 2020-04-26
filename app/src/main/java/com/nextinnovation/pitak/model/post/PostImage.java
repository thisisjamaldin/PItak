package com.nextinnovation.pitak.model.post;

import java.io.Serializable;

public class PostImage implements Serializable {
    private String content;
    private String path;
    private String name;

    public PostImage(String content, String name, String path) {
        this.content = content;
        this.path = path;
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
                "content='" + content + '\'' +
                ", imgType='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}


