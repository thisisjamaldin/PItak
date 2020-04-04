package com.nextinnovation.pitak.model.user;

public class UserCar {
    private long carBrandId;
    private String carBrandName;
    private long carModelId;
    private String carModelName;
    private String carNumber;
    private long carTypeId;
    private String carTypeName;

    public UserCar(long carBrandId, String carBrandName, long carModelId, String carModelName, String carNumber, long carTypeId, String carTypeName) {
        this.carBrandId = carBrandId;
        this.carBrandName = carBrandName;
        this.carModelId = carModelId;
        this.carModelName = carModelName;
        this.carNumber = carNumber;
        this.carTypeId = carTypeId;
        this.carTypeName = carTypeName;
    }

    public long getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(long carBrandId) {
        this.carBrandId = carBrandId;
    }

    public String getCarBrandName() {
        return carBrandName;
    }

    public void setCarBrandName(String carBrandName) {
        this.carBrandName = carBrandName;
    }

    public long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(long carModelId) {
        this.carModelId = carModelId;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public long getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(long carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    @Override
    public String toString() {
        return "UserCar{" +
                "carBrandId=" + carBrandId +
                ", carBrandName='" + carBrandName + '\'' +
                ", carModelId=" + carModelId +
                ", carModelName='" + carModelName + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", carTypeId=" + carTypeId +
                ", carTypeName='" + carTypeName + '\'' +
                '}';
    }
}
