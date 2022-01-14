package com.example.marketingapplication.model;
import com.google.firebase.database.Exclude;

public class ShopsModel {
    public boolean isFavourite = false;
    private String uid, accountType, email, phone, profileImage, storename, timestamp;

    public ShopsModel(){

    }

    public ShopsModel(String uid, String accountType, String email, String phone, String profileImage, String storename, String timestamp) {
        this.uid = uid;
        this.accountType = accountType;
        this.email = email;
        this.phone = phone;
        this.profileImage = profileImage;
        this.storename = storename;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
