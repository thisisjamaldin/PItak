package com.nextinnovation.pitak.model.report;

public class Report {
    private boolean clicked;
    private long id;
    private String name;

    public Report(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
