package com.nextinnovation.pitak.model.user;

public class User {
    private String username;
    private String email;
    private String password;
    private String surname;
    private String name;
    private String patronymic;
    private String userType;
    private String smsCode;
    private String carBrand;
    private String carNumber;
    private String accessToken;

    public User(String username, String email, String password, String surname, String name, String patronymic, String userType, String smsCode, String carBrand, String carNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.userType = userType;
        this.smsCode = smsCode;
        this.carBrand = carBrand;
        this.carNumber = carNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\":\"" + username + '\"' +
                ", \"email\":\"" + email + '\"' +
                ", \"password\":\"" + password + '\"' +
                ", \"surname\":\"" + surname + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"patronymic\":\"" + patronymic + '\"' +
                ", \"userType\":\"" + userType + '\"' +
                ", \"smsCode\":\"" + smsCode + '\"' +
                ", \"carBrand\":\"" + carBrand + '\"' +
                ", \"carNumber\":\"" + carNumber + '\"' +
                '}';
    }
}
