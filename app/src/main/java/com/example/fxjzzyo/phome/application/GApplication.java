package com.example.fxjzzyo.phome.application;

import android.app.Application;

import com.example.fxjzzyo.phome.database.ComDB;
import com.example.fxjzzyo.phome.javabean.Global;
import com.example.fxjzzyo.phome.utils.MyUtil;

/**
 * Created by Administrator on 2017/4/14.
 */

public class GApplication extends Application {
    private ComDB comDBHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        comDBHelper = new ComDB(this);
        comDBHelper.open();
        System.out.println("--------GApplication----------");
        comDBHelper.close();
        //判断网络是否可用
     Global.isNetAvailable =  MyUtil.isNetworkAvailable(this);
    }

}
