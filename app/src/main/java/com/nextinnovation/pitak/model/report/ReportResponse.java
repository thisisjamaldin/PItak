package com.nextinnovation.pitak.model.report;

import java.util.List;

public class ReportResponse {
    private List<Report> result;

    public ReportResponse(List<Report> result) {
        this.result = result;
    }

    public List<Report> getResult() {
        return result;
    }

    public void setResult(List<Report> result) {
        this.result = result;
    }
}
