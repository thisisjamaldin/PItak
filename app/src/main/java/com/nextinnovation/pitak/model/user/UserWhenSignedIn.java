package com.nextinnovation.pitak.model.user;

import java.util.Arrays;

public class UserWhenSignedIn {
    private long id;
    private String username;
    private String surname;
    private String patronymic;
    private String name;
    private String email;
    private String[] roles;
    private String token;
    private String newPassword;
    private UserCar carCommonModel;
    private ProfileRequest profilePhoto;

    public UserWhenSignedIn(long id, String username, String surname, String patronymic, String name, String email, String[] roles, String token, String newPassword, UserCar carCommonModel) {
        this.id = id;
        this.username = username;
        this.surname = surname;
        this.patronymic = patronymic;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.token = token;
        this.newPassword = newPassword;
        this.carCommonModel = carCommonModel;
    }
    public UserWhenSignedIn(long id, String username, String surname, String patronymic, String name, String email, String[] roles, String token, String newPassword) {
        this.id = id;
        this.username = username;
        this.surname = surname;
        this.patronymic = patronymic;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.token = token;
        this.newPassword = newPassword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public UserCar getCarCommonModel() {
        return carCommonModel;
    }

    public void setCarCommonModel(UserCar carCommonModel) {
        this.carCommonModel = carCommonModel;
    }

    public ProfileRequest getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(ProfileRequest profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override
    public String toString() {
        return "UserWhenSignedIn{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + Arrays.toString(roles) +
                ", accessToken='" + token + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", carCommonModel=" + carCommonModel +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }
}
