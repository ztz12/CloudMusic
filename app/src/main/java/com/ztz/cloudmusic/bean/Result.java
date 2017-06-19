package com.ztz.cloudmusic.bean;

import com.loopj.android.image.SmartImageView;

public class Result {
    private String picurl;
    private String desc;
    private String createdAt;
    private String updatedAt;
    private String objectId;

    private SmartImageView smartImageView;

    public Result(String picurl, String desc, String createdAt, String updatedAt, String objectId) {
        this.picurl = picurl;
        this.desc = desc;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.objectId = objectId;
    }

    public String getPicurl() {
        return picurl;
    }



    public SmartImageView getSmartImageView() {
        return smartImageView;
    }

    public void setSmartImageView(SmartImageView smartImageView) {
        this.smartImageView = smartImageView;
    }


    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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
}