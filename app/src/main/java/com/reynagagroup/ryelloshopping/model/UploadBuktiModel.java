package com.reynagagroup.ryelloshopping.model;

import android.widget.TextView;


import java.util.Date;

public class UploadBuktiModel {
    private String atasNama;
    private String id_user;
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

    private boolean ishapus;



    public UploadBuktiModel(){

    }


    public UploadBuktiModel(String atasNama, String id_user, String username, String imageUrl, String bank, String tgl_transfer, String fullnameAddress, String fullAddress, String phone, String email, String pincodeAddress, Date tgl_pesan, int totalAmount, int totalItems, int totalItemsPrice, int savedAmount, String deliveryPrice, boolean ordered, boolean packed, boolean shipped, boolean delivered, boolean canceled, Date ordered_date, Date packed_date, Date shipped_date, Date delivered_date, Date canceled_date, String ket_kirim, String metode_kirim, boolean isfree, boolean ishapus) {
        this.atasNama = atasNama;
        this.id_user = id_user;
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
        this.ishapus = ishapus;
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

    public boolean isIshapus() {
        return ishapus;
    }

    public void setIshapus(boolean ishapus) {
        this.ishapus = ishapus;
    }
}
