package com.reynagagroup.ryelloshopping.model;

public class RewardModel {

    private String title;
    private String expiryDate;
    private String discount;
    private String CouponBody1;
    private String CouponBody2;

    public RewardModel(String title, String expiryDate, String discount, String couponBody1, String couponBody2) {
        this.title = title;
        this.expiryDate = expiryDate;
        this.discount = discount;
        CouponBody1 = couponBody1;
        CouponBody2 = couponBody2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCouponBody1() {
        return CouponBody1;
    }

    public void setCouponBody1(String couponBody1) {
        CouponBody1 = couponBody1;
    }

    public String getCouponBody2() {
        return CouponBody2;
    }

    public void setCouponBody2(String couponBody2) {
        CouponBody2 = couponBody2;
    }
}
