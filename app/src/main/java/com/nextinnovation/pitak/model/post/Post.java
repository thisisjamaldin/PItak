package com.nextinnovation.pitak.model.post;

import com.nextinnovation.pitak.model.car.Car;
import com.nextinnovation.pitak.model.user.UserCar;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private long id;
    private String title;
    private String text;
    private String amountPayment;
    private int numberOfSeat;
    private String fromPlace;
    private String toPlace;
    private Car typeService;
    private long createdBy;
    private UserCar carCommonModel;
    private String mobileNumber;
    private boolean isFavorite;

    public Post(String title, String text, String amountPayment, int numberOfSeat, String fromPlace, String toPlace, Car typeService, long createdBy, String mobileNumber, boolean isFavorite) {
        this.title = title;
        this.text = text;
        this.amountPayment = amountPayment;
        this.numberOfSeat = numberOfSeat;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.typeService = typeService;
        this.createdBy = createdBy;
        this.mobileNumber = mobileNumber;
        this.isFavorite = isFavorite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAmountPayment() {
        return amountPayment;
    }

    public void setAmountPayment(String amountPayment) {
        this.amountPayment = amountPayment;
    }

    public int getNumberOfSeat() {
        return numberOfSeat;
    }

    public void setNumberOfSeat(int numberOfSeat) {
        this.numberOfSeat = numberOfSeat;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public Car getTypeService() {
        return typeService;
    }

    public void setTypeService(Car typeService) {
        this.typeService = typeService;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public UserCar getCarCommonModel() {
        return carCommonModel;
    }

    public void setCarCommonModel(UserCar carCommonModel) {
        this.carCommonModel = carCommonModel;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", amountPayment='" + amountPayment + '\'' +
                ", numberOfSeat=" + numberOfSeat +
                ", fromPlace='" + fromPlace + '\'' +
                ", toPlace='" + toPlace + '\'' +
                ", advertType='" +  + '\'' +
                ", createdBy=" + createdBy +
                ", mobilePhone='" + mobileNumber + '\'' +
                '}';
    }
}
