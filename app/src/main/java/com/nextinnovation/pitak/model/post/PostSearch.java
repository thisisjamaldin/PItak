package com.nextinnovation.pitak.model.post;

import java.util.Arrays;

public class PostSearch {
    private Long byCreatedUserId;
    private String fromPlace;
    private String toPlace;
    private String fromDateTime;
    private String toDateTime;
    private String[] type;

    public PostSearch(Long byCreatedUserId, String fromPlace, String toPlace, String fromDateTime, String toDateTime, String[] type) {
        this.byCreatedUserId = byCreatedUserId;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.type = type;
    }

    public PostSearch() {
    }

    public Long getByCreatedUserId() {
        return byCreatedUserId;
    }

    public void setByCreatedUserId(Long byCreatedUserId) {
        this.byCreatedUserId = byCreatedUserId;
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

    public String getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PostSearch{" +
                "byCreatedUserId=" + byCreatedUserId +
                ", fromPlace='" + fromPlace + '\'' +
                ", toPlace='" + toPlace + '\'' +
                ", fromDateTime='" + fromDateTime + '\'' +
                ", toDateTime='" + toDateTime + '\'' +
                ", type=" + Arrays.toString(type) +
                '}';
    }
}
