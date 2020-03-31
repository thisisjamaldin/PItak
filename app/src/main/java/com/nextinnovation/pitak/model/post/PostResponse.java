package com.nextinnovation.pitak.model.post;

public class PostResponse {
    private PostResult result;
    private String resultCode;
    private String detail;

    public PostResponse(PostResult result, String resultCode, String detail) {
        this.result = result;
        this.resultCode = resultCode;
        this.detail = detail;
    }

    public PostResult getResult() {
        return result;
    }

    public void setResult(PostResult result) {
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

    @Override
    public String toString() {
        return "PostResponse{" +
                "result=" + result.toString() +
                ", resultCode='" + resultCode + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
