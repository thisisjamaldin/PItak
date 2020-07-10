package com.nextinnovation.pitak.model.car;

import com.nextinnovation.pitak.model.user.UserCar;

import java.util.List;

public class NewCarResponse {
    private List<CarCommonModel> result;

    public NewCarResponse(List<CarCommonModel> result) {
        this.result = result;
    }

    public List<CarCommonModel> getResult() {
        return result;
    }

    public void setResult(List<CarCommonModel> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NewCarResponse{" +
                "result=" + result +
                '}';
    }
}
