package com.reynagagroup.ryelloshopping.model;

public class AddressModel {
    private String fullname;
    private String address;
    private String pincode;
    private String phone;
    private Boolean selected;

    public AddressModel(String fullname, String address, String pincode, String phone, Boolean selected) {
        this.fullname = fullname;
        this.address = address;
        this.pincode = pincode;
        this.phone = phone;
        this.selected = selected;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
