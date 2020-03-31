package com.nextinnovation.pitak.model.post;

import java.util.List;

public class PostResult {
    private List<Post> content;
    private int totalElements;

    public PostResult(List<Post> content, int totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<Post> getContent() {
        return content;
    }

    public void setContent(List<Post> content) {
        this.content = content;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "content=" + content +
                ", totalElements=" + totalElements +
                '}';
    }
}
