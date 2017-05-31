package com.example.fxjzzyo.phome.javabean;

/**
 * Created by Administrator on 2017/4/18.
 */

public class PosSensor {

    private int posSenId;
    private int phoneId;

    private String gps;
    private String sensor;
    private String specialFunction;

    public int getPosSenId() {
        return posSenId;
    }

    public void setPosSenId(int posSenId) {
        this.posSenId = posSenId;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getSpecialFunction() {
        return specialFunction;
    }

    public void setSpecialFunction(String specialFunction) {
        this.specialFunction = specialFunction;
    }
}
