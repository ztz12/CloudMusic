package com.ztz.cloudmusic.bean;

/**
 * Created by wqewqe on 2017/6/7.
 */

public class LoginResponse {
    //使用Gson 解析，新建的变量名必须和Json的key一样
    private String sessionToken;
    private String updatedAt;
    private String objectId;
    private String username;
    private String createdAt;
    private boolean emailVerified;
    private boolean mobilePhoneVerified;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMobilePhoneVerified() {
        return mobilePhoneVerified;
    }

    public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
        this.mobilePhoneVerified = mobilePhoneVerified;
    }

    @Override
    public String toString() {
        return "LoginResponse{"+
                "sessionToken="+sessionToken+'\''
                +",updatedAt="+updatedAt+'\''
                +",objectId="+objectId+'\''
                +",username="+username+'\''
                +",createdAt="+createdAt+'\''
                + ",emailVerified="+emailVerified
                +",mobilePhoneVerified="+mobilePhoneVerified+
                '}';

    }
}
