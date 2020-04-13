package com.nextinnovation.pitak.model.post;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private long id;
    private String title;
    private String text;
    @SerializedName("amount_payment")
    private String amountPayment;
    private int numberOfSeat;
    private String fromPlace;
    private String toPlace;
    private String advertType;
    private long createdBy;
    private String mobileNumber;
    private List<PostImage> appFiles;

    public Post(long id, String title, String text, String amountPayment, int numberOfSeat, String fromPlace, String toPlace, String advertType, long createdBy, String mobileNumber) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.amountPayment = amountPayment;
        this.numberOfSeat = numberOfSeat;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.advertType = advertType;
        this.createdBy = createdBy;
        this.mobileNumber = mobileNumber;
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

    public String getAdvertType() {
        return advertType;
    }

    public void setAdvertType(String advertType) {
        this.advertType = advertType;
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

    public List<PostImage> getImgFileList() {
        return appFiles;
    }

    public void setImgFileList(List<PostImage> appFiles) {
        this.appFiles = appFiles;
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
                ", advertType='" + advertType + '\'' +
                ", createdBy=" + createdBy +
                ", mobilePhone='" + mobileNumber + '\'' +
                '}';
    }
}
