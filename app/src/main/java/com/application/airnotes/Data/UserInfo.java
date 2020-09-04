package com.application.airnotes.Data;

import java.io.Serializable;

public class UserInfo implements Serializable {
    String userName="";
    String userEmailId="";
    String mobileNumber="";

    public UserInfo() {
    }

    public UserInfo(String userName, String userEmailId, String mobileNumber) {
        this.userName = userName;
        this.userEmailId = userEmailId;
        this.mobileNumber = mobileNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
