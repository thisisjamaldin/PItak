package com.nextinnovation.pitak.model.post;

import java.util.Arrays;

public class PostSearch {
    private String fromPlace;
    private String toPlace;
    private String title;

    public PostSearch(String fromPlace, String toPlace, String title) {
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.title = title;
    }

    public PostSearch() {
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PostSearch{" +
                ", fromPlace='" + fromPlace + '\'' +
                ", toPlace='" + toPlace + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
