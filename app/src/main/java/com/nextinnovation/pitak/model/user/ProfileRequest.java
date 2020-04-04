package com.nextinnovation.pitak.model.user;

public class ProfileRequest {
    private String profilePhoto;

    public ProfileRequest(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
