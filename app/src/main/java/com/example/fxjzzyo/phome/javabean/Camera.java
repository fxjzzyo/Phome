package com.example.fxjzzyo.phome.javabean;

/**
 * Created by Administrator on 2017/4/18.
 */

public class Camera {

    private int cameraId;
    private int phoneId;

    private String sensorType;
    private String backCamera;
    private String frontCamera;
    private String flashLight;
    private String video;
    private String photoFeature;


    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getBackCamera() {
        return backCamera;
    }

    public void setBackCamera(String backCamera) {
        this.backCamera = backCamera;
    }

    public String getFrontCamera() {
        return frontCamera;
    }

    public void setFrontCamera(String frontCamera) {
        this.frontCamera = frontCamera;
    }

    public String getFlashLight() {
        return flashLight;
    }

    public void setFlashLight(String flashLight) {
        this.flashLight = flashLight;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPhotoFeature() {
        return photoFeature;
    }

    public void setPhotoFeature(String photoFeature) {
        this.photoFeature = photoFeature;
    }
}
