package com.reynagagroup.ryelloshopping.model;

public class HorizontalProductScrollModel {

    private String productID;
    private  String  productImage;
    private String productTitle;
    private String productDescription;
    private  String producPrice;

    public HorizontalProductScrollModel(String productID,String productImage, String productTitle, String productDescription, String producPrice) {
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.producPrice = producPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProducPrice() {
        return producPrice;
    }

    public void setProducPrice(String producPrice) {
        this.producPrice = producPrice;
    }
}
