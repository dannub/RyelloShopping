package com.reynagagroup.ryelloshopping.model;

import java.util.Date;

public class MyOrderItemModel implements Comparable<MyOrderItemModel>{


    private String atasNama;
    private String id_user;
    private String id_nota;
    private String id_item;
    private String username;
    private String imageUrl;
    private String bank;
    private String tgl_transfer;
    private String fullnameAddress;
    private String fullAddress;
    private String phone;
    private String email;
    private String pincodeAddress;
    private Date tgl_pesan;
    private int totalAmount;
    private int totalItems,totalItemsPrice,savedAmount;
    private String deliveryPrice;
    private boolean ordered;
    private boolean packed;
    private boolean shipped;
    private boolean delivered;
    private boolean canceled;
    private Date ordered_date;
    private Date packed_date;
    private Date shipped_date;
    private Date delivered_date;
    private Date canceled_date;
    private String ket_kirim;
    private String metode_kirim;
    private boolean isfree;


    //////cart item
    private String productID;
    private  String productImage;
    private  String productTitle;
    private  String productPrice;
    private  String oriPrice;
    private  Long productQuantity;
    private  Long offersApplied;
    private  Long couponsApplied;
    private String selectedCouponId;
    private String discountedPrice;
    private Long freeCoupon;
    private int ratting = 0;
    private String satuan;

    public MyOrderItemModel(String atasNama, String id_user, String id_nota, String id_item, String username, String imageUrl, String bank, String tgl_transfer, String fullnameAddress, String fullAddress, String phone, String email, String pincodeAddress, Date tgl_pesan, int totalAmount, int totalItems, int totalItemsPrice, int savedAmount, String deliveryPrice, boolean ordered, boolean packed, boolean shipped, boolean delivered, boolean canceled, Date ordered_date, Date packed_date, Date shipped_date, Date delivered_date, Date canceled_date, String ket_kirim, String metode_kirim, boolean isfree, String productID, String productImage, String productTitle, String productPrice, String oriPrice, Long productQuantity, Long offersApplied, Long couponsApplied, String selectedCouponId, String discountedPrice,Long freeCoupon, int ratting,String satuan) {
        this.atasNama = atasNama;
        this.id_user = id_user;
        this.id_nota = id_nota;
        this.id_item = id_item;
        this.username = username;
        this.imageUrl = imageUrl;
        this.bank = bank;
        this.tgl_transfer = tgl_transfer;
        this.fullnameAddress = fullnameAddress;
        this.fullAddress = fullAddress;
        this.phone = phone;
        this.email = email;
        this.pincodeAddress = pincodeAddress;
        this.tgl_pesan = tgl_pesan;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.totalItemsPrice = totalItemsPrice;
        this.savedAmount = savedAmount;
        this.deliveryPrice = deliveryPrice;
        this.ordered = ordered;
        this.packed = packed;
        this.shipped = shipped;
        this.delivered = delivered;
        this.canceled = canceled;
        this.ordered_date = ordered_date;
        this.packed_date = packed_date;
        this.shipped_date = shipped_date;
        this.delivered_date = delivered_date;
        this.canceled_date = canceled_date;
        this.ket_kirim = ket_kirim;
        this.metode_kirim = metode_kirim;
        this.isfree = isfree;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.oriPrice = oriPrice;
        this.productQuantity = productQuantity;
        this.offersApplied = offersApplied;
        this.couponsApplied = couponsApplied;
        this.selectedCouponId = selectedCouponId;
        this.discountedPrice = discountedPrice;
        this.freeCoupon = freeCoupon;
        this.ratting = ratting;
        this.satuan = satuan;
    }

    public Long getFreeCoupon() {
        return freeCoupon;
    }

    public void setFreeCoupon(Long freeCoupon) {
        this.freeCoupon = freeCoupon;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getAtasNama() {
        return atasNama;
    }

    public void setAtasNama(String atasNama) {
        this.atasNama = atasNama;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_nota() {
        return id_nota;
    }

    public void setId_nota(String id_nota) {
        this.id_nota = id_nota;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getTgl_transfer() {
        return tgl_transfer;
    }

    public void setTgl_transfer(String tgl_transfer) {
        this.tgl_transfer = tgl_transfer;
    }

    public String getFullnameAddress() {
        return fullnameAddress;
    }

    public void setFullnameAddress(String fullnameAddress) {
        this.fullnameAddress = fullnameAddress;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPincodeAddress() {
        return pincodeAddress;
    }

    public void setPincodeAddress(String pincodeAddress) {
        this.pincodeAddress = pincodeAddress;
    }

    public Date getTgl_pesan() {
        return tgl_pesan;
    }

    public void setTgl_pesan(Date tgl_pesan) {
        this.tgl_pesan = tgl_pesan;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public void setTotalItemsPrice(int totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Date getOrdered_date() {
        return ordered_date;
    }

    public void setOrdered_date(Date ordered_date) {
        this.ordered_date = ordered_date;
    }

    public Date getPacked_date() {
        return packed_date;
    }

    public void setPacked_date(Date packed_date) {
        this.packed_date = packed_date;
    }

    public Date getShipped_date() {
        return shipped_date;
    }

    public void setShipped_date(Date shipped_date) {
        this.shipped_date = shipped_date;
    }

    public Date getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(Date delivered_date) {
        this.delivered_date = delivered_date;
    }

    public Date getCanceled_date() {
        return canceled_date;
    }

    public void setCanceled_date(Date canceled_date) {
        this.canceled_date = canceled_date;
    }

    public String getKet_kirim() {
        return ket_kirim;
    }

    public void setKet_kirim(String ket_kirim) {
        this.ket_kirim = ket_kirim;
    }

    public String getMetode_kirim() {
        return metode_kirim;
    }

    public void setMetode_kirim(String metode_kirim) {
        this.metode_kirim = metode_kirim;
    }

    public boolean isIsfree() {
        return isfree;
    }

    public void setIsfree(boolean isfree) {
        this.isfree = isfree;
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

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(Long offersApplied) {
        this.offersApplied = offersApplied;
    }

    public Long getCouponsApplied() {
        return couponsApplied;
    }

    public void setCouponsApplied(Long couponsApplied) {
        this.couponsApplied = couponsApplied;
    }

    public String getSelectedCouponId() {
        return selectedCouponId;
    }

    public void setSelectedCouponId(String selectedCouponId) {
        this.selectedCouponId = selectedCouponId;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getRatting() {
        return ratting;
    }

    public void setRatting(int ratting) {
        this.ratting = ratting;
    }

    @Override
    public int compareTo(MyOrderItemModel o) {
        if (getTgl_pesan() == null || o.getTgl_pesan() == null)
            return 0;
        return getTgl_pesan().compareTo(o.getTgl_pesan());
    }
}
