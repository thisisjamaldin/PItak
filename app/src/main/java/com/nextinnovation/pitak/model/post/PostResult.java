package com.nextinnovation.pitak.model.post;

import java.util.List;

public class PostResult {
    private List<AppAdvertModel> content;
    private long totalElements;

    public PostResult(List<AppAdvertModel> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<AppAdvertModel> getContent() {
        return content;
    }

    public void setContent(List<AppAdvertModel> content) {
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
