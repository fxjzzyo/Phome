package com.example.fxjzzyo.phome.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/4/13.
 */

public class SharedPreferenceUtil {
public static final String SPF_USER = "user";
public static final String KEY_NAME = "name";
public static final String KEY_PASSWORD = "password";
public static final String IS_AUTO_LOGIN = "autoLogin";

    /**
     * 存储键值对
     * @param context
     * @param key
     * @param data
     */
    public static void saveData(Context context, String key,String data){
        SharedPreferences spf = context.getSharedPreferences(SPF_USER, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString(key, data);
        editor.commit();

    }

    /**
     * 根据键，获取值
     * @param context
     * @param key
     * @return
     */
    public static String getData(Context context, String key){
        SharedPreferences spf = context.getSharedPreferences(SPF_USER, context.MODE_PRIVATE);
        String value = spf.getString(key, null);
        return value;
    }
}
