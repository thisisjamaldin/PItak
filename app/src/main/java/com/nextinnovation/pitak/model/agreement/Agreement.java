package com.nextinnovation.pitak.model.agreement;

public class Agreement {
    private long id;
    private String responseHtml;

    public Agreement(long id, String responseHtml) {
        this.id = id;
        this.responseHtml = responseHtml;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResponseHtml() {
        return responseHtml;
    }

    public void setResponseHtml(String responseHtml) {
        this.responseHtml = responseHtml;
    }
}
