package com.nextinnovation.pitak.model.user;

import com.nextinnovation.pitak.model.car.Car;

import java.io.Serializable;

public class UserCar implements Serializable {
    private Car carBrand;
    private Car carModel;
    private String carNumber;
    private Car carType;

    public UserCar(Car carBrand, Car carModel, String carNumber, Car carType) {
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carType = carType;
    }

    public Car getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(Car carBrand) {
        this.carBrand = carBrand;
    }

    public Car getCarModel() {
        return carModel;
    }

    public void setCarModel(Car carModel) {
        this.carModel = carModel;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Car getCarType() {
        return carType;
    }

    public void setCarType(Car carType) {
        this.carType = carType;
    }

    @Override
    public String toString() {
        return "UserCar{" +
                "carBrand=" + carBrand +
                ", carModel=" + carModel +
                ", carNumber='" + carNumber + '\'' +
                ", carType=" + carType +
                '}';
    }
}
