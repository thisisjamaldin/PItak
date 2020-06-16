package com.nextinnovation.pitak.model.user;

import com.nextinnovation.pitak.model.car.Car;

import java.io.Serializable;
import java.util.List;

public class UserCar implements Serializable {
    private long id;
    private Car carBrand;
    private Car carModel;
    private String carNumber;
    private Car carType;
    private List<ProfileRequest> carFiles;
    private int carryCapacity;

    public UserCar(Car carBrand, Car carModel, String carNumber, Car carType) {
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carType = carType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<ProfileRequest> getCarFiles() {
        return carFiles;
    }

    public void setCarFiles(List<ProfileRequest> carFiles) {
        this.carFiles = carFiles;
    }

    public int getCarryCapacity() {
        return carryCapacity;
    }

    public void setCarryCapacity(int carryCapacity) {
        this.carryCapacity = carryCapacity;
    }

    @Override
    public String toString() {
        return "UserCar{" +
                "id=" + id +
                ", carBrand=" + carBrand +
                ", carModel=" + carModel +
                ", carNumber='" + carNumber + '\'' +
                ", carType=" + carType +
                ", carFiles=" + carFiles +
                ", carryCapacity=" + carryCapacity +
                '}';
    }
}
