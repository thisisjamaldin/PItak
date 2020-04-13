package com.nextinnovation.pitak.model.user;

public class ProfileResponse {
    private ProfileRequest result;
    private String resultCode;
    private String detail;

    public ProfileRequest getResult() {
        return result;
    }

    public void setResult(ProfileRequest result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
