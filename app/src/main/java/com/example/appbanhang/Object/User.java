package com.example.appbanhang.Object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String UserName;
    private String UserPassword;
    private String AccountType;
    private String UserEmail;
    private String UserAddress;
    private String UserGender;
    private String UserPhoneNumber;
    private String UserFullName;
    private String UserDate;
    private String UserPhoto;
    private String UserBackground;

    public User() {
    }

    public User(String userName, String userPassword, String accountType, String userEmail, String userAddress, String userGender, String userPhoneNumber, String userFullName, String userDate, String userPhoto, String userBackground) {
        UserName = userName;
        UserPassword = userPassword;
        AccountType = accountType;
        UserEmail = userEmail;
        UserAddress = userAddress;
        UserGender = userGender;
        UserPhoneNumber = userPhoneNumber;
        UserFullName = userFullName;
        UserDate = userDate;
        UserPhoto = userPhoto;
        UserBackground = userBackground;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String userAddress) {
        UserAddress = userAddress;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public String getUserPhoneNumber() {
        return UserPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        UserPhoneNumber = userPhoneNumber;
    }

    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public String getUserDate() {
        return UserDate;
    }

    public void setUserDate(String userDate) {
        UserDate = userDate;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public String getUserBackground() {
        return UserBackground;
    }

    public void setUserBackground(String userBackground) {
        UserBackground = userBackground;
    }


}
