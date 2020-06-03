package com.nextinnovation.pitak.model.post;

import java.util.List;

public class PostResult {
    private List<Post> content;
    private long totalElements;

    public PostResult(List<Post> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<Post> getContent() {
        return content;
    }

    public void setContent(List<Post> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
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
