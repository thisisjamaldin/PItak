package com.nextinnovation.pitak.model.agreement;

public class AgreementResponse {
    private Agreement result;

    public AgreementResponse(Agreement result) {
        this.result = result;
    }

    public Agreement getResult() {
        return result;
    }

    public void setResult(Agreement result) {
        this.result = result;
    }
}
