package com.example.fxjzzyo.phome.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fxjzzyo.phome.javabean.Phone;
/**
 *  "core_id int, "+
 "network_id int, "+
 "screen_info_id int, "+

 "system_hardware_id int, "+
 "camera_info_id int, "+
 "phone_body_info_id int, "+
 "media_info_id int, "+
 "position_sensor_info_id int, "+
 "comment_id int, " +


 contentValues.put("core_id",phone.getPhoneCoreId());
 contentValues.put("network_id",phone.getPhoneNetworkId());
 contentValues.put("screen_info_id",phone.getPhoneScreenInfoId());

 contentValues.put("system_hardware_id",phone.getPhoneHardwareId());
 contentValues.put("camera_info_id",phone.getPhoneCameraInfoId());
 contentValues.put("phone_body_info_id",phone.getPhoneBodyInfoId());
 contentValues.put("media_info_id",phone.getPhoneMediaInfoId());
 contentValues.put("position_sensor_info_id",phone.getPhonePositionSensorInfoId());
 contentValues.put("comment_id",phone.getPhoneCommentId());
 */

/**
 * Created by Administrator on 2017/4/13.
 */

public class DBHelper extends SQLiteOpenHelper {
    /**
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static final String DB_NAME = "phome.db";
    private static final String TBL_NAME = "phone";
    private static final String CREATE_TBL = " create table "
            + TBL_NAME+"(id int primary key autoincrement," +
            "phone_id int, "+
            "phone_name text, " +
            "phone_price text, "+
            "phone_brand text, " +


            "error_info text, " +
            "market_share text, "+


            "rate text, "+
            "love_count int, "+
            "comment_count int, "+
            ") ";

    private SQLiteDatabase db;

    DBHelper(Context c) {
        super(c, DB_NAME, null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TBL);
        this.db = db;
    }
    public void insert(Phone phone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone_id",phone.getPhoneId());
        contentValues.put("phone_name",phone.getPhoneName());
        contentValues.put("phone_price",phone.getPhonePrice());
        contentValues.put("phone_brand",phone.getPhoneBrand());


        contentValues.put("error_info",phone.getPhoneErrorInfo());
        contentValues.put("market_share",phone.getPhoneMarketShare());


        contentValues.put("rate",phone.getPhoneRate());
        contentValues.put("love_count",phone.getPhoneLoveCount());
        contentValues.put("comment_count",phone.getPhoneCommentCount());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TBL_NAME, null, contentValues);
        db.close();
    }
    public Cursor query() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
        return c;
    }
    public void del(int id) {
        if (db == null)
            db = getWritableDatabase();
        db.delete(TBL_NAME, "_id=?", new String[] { String.valueOf(id) });
    }
    public void close() {
        if (db != null)
            db.close();
    }
}
