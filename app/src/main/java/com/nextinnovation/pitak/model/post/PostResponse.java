package com.nextinnovation.pitak.model.post;

public class PostResponse {
    private PostResult result;
    private String resultCode;

    public PostResponse(PostResult result, String resultCode) {
        this.result = result;
        this.resultCode = resultCode;
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


    @Override
    public String toString() {
        return "PostResponse{" +
                "result=" + result.toString() +
                ", resultCode='" + resultCode + '\'' +
                '}';
    }
}
