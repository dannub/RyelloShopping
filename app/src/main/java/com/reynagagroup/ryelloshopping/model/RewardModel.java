package com.reynagagroup.ryelloshopping.model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class RewardModel {

    private String type;
    private String lowerLimit;
    private String upperLimit;
    private String discount;
    private String CouponBody;
    private Timestamp validity;

    public RewardModel(String type, String lowerLimit, String upperLimit, String discount, String couponBody, Timestamp validity) {
        this.type = type;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.discount = discount;
        this.CouponBody = couponBody;
        this.validity = validity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCouponBody() {
        return CouponBody;
    }

    public void setCouponBody(String couponBody) {
        this.CouponBody = couponBody;
    }


    public Timestamp getValidity() {
        return validity;
    }

    public void setValidity(Timestamp validity) {
        this.validity = validity;
    }
}
