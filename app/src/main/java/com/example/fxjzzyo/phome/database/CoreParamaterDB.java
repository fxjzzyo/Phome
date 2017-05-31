package com.example.fxjzzyo.phome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fxjzzyo.phome.javabean.CoreParamater;

import java.util.ArrayList;

import static com.example.fxjzzyo.phome.database.PhoneDB.KEY_BRAND;

/**
 * Created by Administrator on 2017/4/14.
 */

public class CoreParamaterDB {
    /**
     *  "core_id int, "+
     "phone_id int, "+
     "screen_size text, " +
     "cpu text, "+
     "ram text, " +


     "storage_capacity text, " +
     "back_camera_pixel text, "+
     "battery_capacity text, "+
     */
    public static final String KEY_PHONE_ID = "phone_id";
    public static final String KEY_CORE_ID = "core_id";
    public static final String KEY_SCREEN_SIZE = "screen_size";
    public static final String KEY_CPU = "cpu";
    public static final String KEY_RAM= "ram";
    public static final String KEY_STORAGE= "storage_capacity";
    public static final String KEY_BACK_CAMERA= "back_camera_pixel";
    public static final String KEY_BATTERY= "battery_capacity";

    private static final String TAG = "CoreParamaterDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    // private static final String DATABASE_NAME = "Fortrun_Ticket11";
    static final String SQLITE_TABLE = "CoreParamaterTable";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {

            super(context, ComDB.DATABASE_NAME, null, ComDB.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public CoreParamaterDB(Context ctx) {
        this.mCtx = ctx;
    }

    public CoreParamaterDB open() throws SQLException {

        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
        if(mDb!=null&&mDb.isOpen())
        {
            mDb.close();
        }
    }

    /**
     *
     * 插入一个核心参数
     * @param coreParamater
     * @return
     */
    public long insertCore(CoreParamater coreParamater) {
        long createResult = 0;
        if(coreParamater!=null)
        {

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_PHONE_ID,coreParamater.getPhoneId());
            contentValues.put(KEY_CORE_ID,coreParamater.getCoreId());
            contentValues.put(KEY_CPU,coreParamater.getCpuInfo());
            contentValues.put(KEY_RAM,coreParamater.getRAMSize());
            contentValues.put(KEY_SCREEN_SIZE,coreParamater.getScreenSize());

            contentValues.put(KEY_BACK_CAMERA,coreParamater.getBackCameraPixel());
            contentValues.put(KEY_BATTERY,coreParamater.getBatteryCapacity());
            contentValues.put(KEY_STORAGE,coreParamater.getStorageCapacity());

            try {
                createResult = mDb.insert(SQLITE_TABLE, null, contentValues);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return createResult;
    }

    /**
     * 删除表的全部字段数据
     * @return
     */
    public boolean deleteAllStudents() {
        int doneDelete = 0;
        try {
            doneDelete = mDb.delete(SQLITE_TABLE, null, null);
            Log.w(TAG, Integer.toString(doneDelete));
            Log.e("doneDelete", doneDelete + "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return doneDelete > 0;
    }

    /**
     * 根据名称删除表中的数据
     * @return
     *//*
    public boolean deletePhoneById(int phoneId) {
        int isDelete;
        String [] phoneIds;
         phoneIds = new String[] { "phone_id" };
        isDelete = mDb.delete(SQLITE_TABLE, KEY_PHONE_ID + "=?", phoneIds);

        return isDelete > 0;
    }*/

    /**
     * 插入一些
     * @param coreParamaters
     */
    public void insertSomePhones(ArrayList<CoreParamater> coreParamaters) {
        if(coreParamaters!=null)
        {
            for (CoreParamater coreParamater :
                    coreParamaters) {
                insertCore(coreParamater);
            }
        }
    }


    /**
     * 获取表中的所有字段
     * @return
     */
    public ArrayList<CoreParamater> fetchAll() {

        ArrayList<CoreParamater> coreParamaters = new ArrayList<CoreParamater>();
        Cursor mCursor = null;
        mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_PHONE_ID, KEY_CORE_ID,
                KEY_SCREEN_SIZE, KEY_CPU,KEY_RAM,KEY_STORAGE,KEY_BACK_CAMERA,KEY_BATTERY}, null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                CoreParamater coreParamater = new CoreParamater();
                    coreParamater.setCoreId(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_CORE_ID)));
                    coreParamater.setScreenSize(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_SCREEN_SIZE)));
                    coreParamater.setPhoneId(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_PHONE_ID)));
                    coreParamater.setCpuInfo(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_CPU)));
                    coreParamater.setRAMSize(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_RAM)));

                    coreParamater.setStorageCapacity(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_STORAGE)));
                    coreParamater.setBackCameraPixel(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_BACK_CAMERA)));
                    coreParamater.setBatteryCapacity(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_BATTERY)));
                coreParamaters.add(coreParamater);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return coreParamaters;
    }
}
