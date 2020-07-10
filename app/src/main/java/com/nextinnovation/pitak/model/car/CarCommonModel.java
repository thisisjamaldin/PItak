package com.nextinnovation.pitak.model.car;

import com.nextinnovation.pitak.model.user.ProfileRequest;
import com.nextinnovation.pitak.model.user.UserCar;

import java.io.Serializable;
import java.util.List;

public class CarCommonModel implements Serializable {
    private UserCar carCommonModel;
    private List<CarImage> attachmentModels;

    public CarCommonModel(UserCar carCommonModel, List<CarImage> attachmentModels) {
        this.carCommonModel = carCommonModel;
        this.attachmentModels = attachmentModels;
    }

    public UserCar getCarCommonModel() {
        return carCommonModel;
    }

    public void setCarCommonModel(UserCar carCommonModel) {
        this.carCommonModel = carCommonModel;
    }

    public List<CarImage> getAttachmentModels() {
        return attachmentModels;
    }

    public void setAttachmentModels(List<CarImage> attachmentModels) {
        this.attachmentModels = attachmentModels;
    }

    @Override
    public String toString() {
        return "CarCommonModel{" +
                "carCommonModel=" + carCommonModel +
                ", attachmentModels=" + attachmentModels +
                '}';
    }
}
