package com.example.fxjzzyo.phome.javabean;

/**
 * Created by Administrator on 2017/4/12.
 */

public class User {

    private int userId;
    private String userName;
    private String userPassword;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "用户id:" + this.userId + " 用户名：" + this.userName + " 密码：" + this.userPassword;
    }
}
