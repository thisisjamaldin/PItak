package com.nextinnovation.pitak.model.report;

public class ReportRequest {
    private String additionalDescription;
    private long advertId;
    private long reportTypeId;

    public ReportRequest(String additionalDescription, long advertId, long reportTypeId) {
        this.additionalDescription = additionalDescription;
        this.advertId = advertId;
        this.reportTypeId = reportTypeId;
    }

    public String getAdditionalDescription() {
        return additionalDescription;
    }

    public void setAdditionalDescription(String additionalDescription) {
        this.additionalDescription = additionalDescription;
    }

    public long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(long advertId) {
        this.advertId = advertId;
    }

    public long getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(long reportTypeId) {
        this.reportTypeId = reportTypeId;
    }
}
