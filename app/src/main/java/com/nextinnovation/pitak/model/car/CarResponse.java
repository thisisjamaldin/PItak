package com.nextinnovation.pitak.model.car;

import java.util.List;

public class CarResponse {
    private List<Car> result;

    public CarResponse(List<Car> result) {
        this.result = result;
    }

    public CarResponse() {
    }

    public List<Car> getResult() {
        return result;
    }

    public void setResult(List<Car> result) {
        this.result = result;
    }
}
