package com.nextinnovation.pitak.model.post;

import com.nextinnovation.pitak.model.car.CarImage;

import java.io.Serializable;
import java.util.List;

public class AppAdvertModel implements Serializable {

    private Post appAdvertModel;
    private List<CarImage> attachmentModels;

    public AppAdvertModel(Post appAdvertModel, List<CarImage> attachmentModels) {
        this.appAdvertModel = appAdvertModel;
        this.attachmentModels = attachmentModels;
    }

    public Post getAppAdvertModel() {
        return appAdvertModel;
    }

    public void setAppAdvertModel(Post appAdvertModel) {
        this.appAdvertModel = appAdvertModel;
    }

    public List<CarImage> getAttachmentModels() {
        return attachmentModels;
    }

    public void setAttachmentModels(List<CarImage> attachmentModels) {
        this.attachmentModels = attachmentModels;
    }

    @Override
    public String toString() {
        return "AppAdvertModel{" +
                "appAdvertModel=" + appAdvertModel +
                '}';
    }
}
