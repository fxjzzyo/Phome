package com.example.fxjzzyo.phome.javabean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 */
public class Phone implements Parcelable {

    private int phoneId;//服务器端的phoneid
    private String phoneName;
    private String phonePrice;
    private String phoneErrorInfo;
    private String phoneMarketShare;
    private String phoneRate;//手机总评分

    private String phoneBrand;
    private int phoneCommentCount;//手机评论数
    private int phoneLoveCount;//手机收藏人数

public Phone(){

}


    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhonePrice() {
        return phonePrice;
    }

    public void setPhonePrice(String phonePrice) {
        this.phonePrice = phonePrice;
    }


    public String getPhoneErrorInfo() {
        return phoneErrorInfo;
    }

    public void setPhoneErrorInfo(String phoneErrorInfo) {
        this.phoneErrorInfo = phoneErrorInfo;
    }

    public String getPhoneMarketShare() {
        return phoneMarketShare;
    }

    public void setPhoneMarketShare(String phoneMarketShare) {
        this.phoneMarketShare = phoneMarketShare;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }




    public String getPhoneRate() {
        return phoneRate;
    }

    public void setPhoneRate(String phoneRate) {
        this.phoneRate = phoneRate;
    }

    public int getPhoneCommentCount() {
        return phoneCommentCount;
    }

    public void setPhoneCommentCount(int phoneCommentCount) {
        this.phoneCommentCount = phoneCommentCount;
    }

    public int getPhoneLoveCount() {
        return phoneLoveCount;
    }

    public void setPhoneLoveCount(int phoneLoveCount) {
        this.phoneLoveCount = phoneLoveCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //注意：写的顺序与读的顺序必须一致
        parcel.writeInt(getPhoneId());
        parcel.writeInt(getPhoneCommentCount());
        parcel.writeInt(getPhoneLoveCount());

        parcel.writeString(getPhoneBrand());
        parcel.writeString(getPhoneErrorInfo());
        parcel.writeString(getPhoneName());
        parcel.writeString(getPhonePrice());
        parcel.writeString(getPhoneRate());
        parcel.writeString(getPhoneMarketShare());

    }
    protected Phone(Parcel in) {
        //注意：读的顺序与写的顺序必须一致
        phoneId = in.readInt();
        phoneCommentCount = in.readInt();
        phoneLoveCount = in.readInt();

        phoneBrand = in.readString();
        phoneErrorInfo = in.readString();
        phoneName = in.readString();
        phonePrice = in.readString();
        phoneRate = in.readString();
        phoneMarketShare = in.readString();


    }

    public static final Creator<Phone> CREATOR = new Creator<Phone>() {
        @Override
        public Phone createFromParcel(Parcel in) {
            return new Phone(in);
        }

        @Override
        public Phone[] newArray(int size) {
            return new Phone[size];
        }
    };
}
