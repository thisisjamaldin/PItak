package com.nextinnovation.pitak.model.post;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

public class PostCreate {
    private String title;
    private String text;
    @SerializedName("amount_payment")
    private String amountPayment;
    private int numberOfSeat;
    private String fromPlace;
    private String toPlace;
    private String advertType;
    private String sendDateTime;

    public PostCreate(String title, String text, String amountPayment, int numberOfSeat, String fromPlace, String toPlace, String advertType, String sendDateTime) {
        this.title = title;
        this.text = text;
        this.amountPayment = amountPayment;
        this.numberOfSeat = numberOfSeat;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.advertType = advertType;
        this.sendDateTime = convertDate(sendDateTime);
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

    public String getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(String sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    private String convertDate(String dateString){
        SimpleDateFormat sdfIn = new SimpleDateFormat("EEE, dd MMM yyyyHH:mm", Locale.getDefault());
        Date date = new Date();
        try {
            date = sdfIn.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdfOut.format(date);
    }
}
