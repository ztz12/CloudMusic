package com.ztz.cloudmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wqewqe on 2017/6/9.
 */

public class Home implements Parcelable{
    private int type;
    private String name;
    ArrayList<HomeResponse.ResultsBean.PlayListBean> playListBeen=new ArrayList<>();
    public Home(){

    }

    protected Home(Parcel in) {
        type = in.readInt();
        name = in.readString();
        playListBeen = in.createTypedArrayList(HomeResponse.ResultsBean.PlayListBean.CREATOR);
    }

    public static final Creator<Home> CREATOR = new Creator<Home>() {
        @Override
        public Home createFromParcel(Parcel in) {
            return new Home(in);
        }

        @Override
        public Home[] newArray(int size) {
            return new Home[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<HomeResponse.ResultsBean.PlayListBean> getPlayListBeen() {
        return playListBeen;
    }

    public void setPlayListBeen(ArrayList<HomeResponse.ResultsBean.PlayListBean> playListBeen) {
        this.playListBeen = playListBeen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeTypedList(playListBeen);
    }
}
