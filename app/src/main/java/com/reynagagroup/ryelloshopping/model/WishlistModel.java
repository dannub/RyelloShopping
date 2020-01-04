package com.reynagagroup.ryelloshopping.model;

public class WishlistModel {

    private String  productImage;
    private String productID;
    private String productTitle;
    private long freeCoupons;
    private String ratting;
    private long totalRattings;
    private String productPrice;
    private String cuttedPrice;
    private Boolean COD;
    private Boolean inStock;

    public WishlistModel(String productID,String productImage, String productTitle, long freeCoupons, String ratting, long totalRattings, String productPrice, String cuttedPrice, Boolean COD, Boolean inStock) {
        this.productImage = productImage;
        this.productID = productID;
        this.productTitle = productTitle;
        this.freeCoupons = freeCoupons;
        this.ratting = ratting;
        this.totalRattings = totalRattings;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.COD = COD;
        this.inStock = inStock;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
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

    public long getFreeCoupons() {
        return freeCoupons;
    }

    public void setFreeCoupons(long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }

    public long getTotalRattings() {
        return totalRattings;
    }

    public void setTotalRattings(long totalRattings) {
        this.totalRattings = totalRattings;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Boolean getCOD() {
        return COD;
    }

    public void setCOD(Boolean COD) {
        this.COD = COD;
    }
}
