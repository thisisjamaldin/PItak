package com.nextinnovation.pitak.model.post;

import com.nextinnovation.pitak.model.car.Car;

import java.util.Arrays;
import java.util.List;

public class PostSearch {
    private String fromPlace;
    private String toPlace;
    private String title;
    private List<Car> type;

    public PostSearch() {
    }

    public List<Car> getType() {
        return type;
    }

    public void setType(List<Car> type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "PostSearch{" +
                ", fromPlace='" + fromPlace + '\'' +
                ", toPlace='" + toPlace + '\'' +
                '}';
    }
}
