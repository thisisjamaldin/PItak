package com.nextinnovation.pitak.model.user;

import com.nextinnovation.pitak.model.car.Car;
import com.nextinnovation.pitak.model.car.CarCommonModel;

public class EditUser {
    private CarCommonModel carCommonModel;
    private Car cityModel, countryModel;
    private String email;
    private long id;
    private String name;
    private String newPassword;
    private String patronymic;
    private String surname;
    private String userType;
    private String username;

    public EditUser(CarCommonModel carCommonModel, Car cityModel, Car countryModel, String email, long id, String name, String newPassword, String patronymic, String surname,String userType, String username) {
        this.carCommonModel = carCommonModel;
        this.cityModel = cityModel;
        this.countryModel = countryModel;
        this.email = email;
        this.id = id;
        this.name = name;
        this.newPassword = newPassword;
        this.patronymic = patronymic;
        this.surname = surname;
        this.userType = userType;
        this.username = username;
    }

    public CarCommonModel getCarCommonModel() {
        return carCommonModel;
    }

    public void setCarCommonModel(CarCommonModel carCommonModel) {
        this.carCommonModel = carCommonModel;
    }

    public Car getCityModel() {
        return cityModel;
    }

    public void setCityModel(Car cityModel) {
        this.cityModel = cityModel;
    }

    public Car getCountryModel() {
        return countryModel;
    }

    public void setCountryModel(Car countryModel) {
        this.countryModel = countryModel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "EditUser{" +
                "carCommonModel=" + carCommonModel +
                ", cityModel=" + cityModel +
                ", countryModel=" + countryModel +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
