package com.nextinnovation.pitak.model.car;

import com.nextinnovation.pitak.model.user.UserCar;

import java.util.List;

public class NewCarResponse {
    private List<UserCar> result;

    public NewCarResponse(List<UserCar> result) {
        this.result = result;
    }

    public List<UserCar> getResult() {
        return result;
    }

    public void setResult(List<UserCar> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "NewCarResponse{" +
                "result=" + result +
                '}';
    }
}
