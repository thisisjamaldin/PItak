package com.nextinnovation.pitak.model.car;

import com.nextinnovation.pitak.model.user.ProfileRequest;

import java.io.Serializable;

public class CarImage  implements Serializable {
    private ProfileRequest appFile;

    public CarImage(ProfileRequest appFile) {
        this.appFile = appFile;
    }

    public ProfileRequest getAppFile() {
        return appFile;
    }

    public void setAppFile(ProfileRequest appFile) {
        this.appFile = appFile;
    }

    @Override
    public String toString() {
        return "CarImage{" +
                "appFile=" + appFile +
                '}';
    }
}
