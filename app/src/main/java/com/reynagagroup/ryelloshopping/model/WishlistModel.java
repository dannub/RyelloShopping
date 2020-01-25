package com.reynagagroup.ryelloshopping.model;

import java.util.ArrayList;

public class WishlistModel {

    private String  productImage;
    private String productID;
    private String productTitle;
    private long freeCoupons;
    private String ratting;
    private long totalRattings;
    private String productPrice;
    private String oriPrice;
    private Boolean COD;
    private Boolean inStock;
    private long offersApplied;
    private ArrayList<String> tags;

    public WishlistModel( String productID,String productImage, String productTitle, long freeCoupons, String ratting, long totalRattings, String productPrice, String oriPrice, Boolean COD, Boolean inStock, long offersApplied) {
        this.productImage = productImage;
        this.productID = productID;
        this.productTitle = productTitle;
        this.freeCoupons = freeCoupons;
        this.ratting = ratting;
        this.totalRattings = totalRattings;
        this.productPrice = productPrice;
        this.oriPrice = oriPrice;
        this.COD = COD;
        this.inStock = inStock;
        this.offersApplied = offersApplied;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }

    public Boolean getCOD() {
        return COD;
    }

    public void setCOD(Boolean COD) {
        this.COD = COD;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(long offersApplied) {
        this.offersApplied = offersApplied;
    }
}
