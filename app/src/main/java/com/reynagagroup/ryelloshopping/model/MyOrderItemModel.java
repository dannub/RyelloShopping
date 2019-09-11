package com.reynagagroup.ryelloshopping.model;

public class MyOrderItemModel {


    private int productImage;
    private  int ratting;
    private String productTitle;
    private String deliverStatus;

    public MyOrderItemModel(int productImage,int ratting, String productTitle, String deliverStatus) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.deliverStatus = deliverStatus;
        this.ratting = ratting;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public int getRatting() {
        return ratting;
    }

    public void setRatting(int ratting) {
        this.ratting = ratting;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }
}
