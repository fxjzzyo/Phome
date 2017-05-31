package com.example.fxjzzyo.phome.javabean;

/**
 * Created by Administrator on 2017/4/18.
 */

public class Media {
    private int mediaId;
    private int phoneId;

    private String videoFormat;
    private String musicFormat;
    private String picFormat;
    private String docFormat;


    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    public String getMusicFormat() {
        return musicFormat;
    }

    public void setMusicFormat(String musicFormat) {
        this.musicFormat = musicFormat;
    }

    public String getPicFormat() {
        return picFormat;
    }

    public void setPicFormat(String picFormat) {
        this.picFormat = picFormat;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }
}
