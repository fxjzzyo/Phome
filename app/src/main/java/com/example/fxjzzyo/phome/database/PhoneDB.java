package com.example.fxjzzyo.phome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fxjzzyo.phome.javabean.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class PhoneDB {

    public static final String KEY_PHONE_ID = "phone_id";
    public static final String KEY_NAME = "phone_name";
    public static final String KEY_PRICE = "phone_price";
    public static final String KEY_BRAND = "phone_brand";
    public static final String KEY_ERROR = "error_info";
    public static final String KEY_MARKET_SHARE = "market_share";
    public static final String KEY_RATE = "rate";
    public static final String KEY_LOVE_COUNT = "love_count";
    public static final String KEY_COMMENT_COUNT = "comment_count";

    private static final String TAG = "PhoneDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    // private static final String DATABASE_NAME = "Fortrun_Ticket11";
    static final String SQLITE_TABLE = "PhoneTable";

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

    public PhoneDB(Context ctx) {
        this.mCtx = ctx;
    }

    public PhoneDB open() throws SQLException {

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
     * 插入一个手机
     * @param phone
     * @return
     */
    public long insertPhone(Phone phone) {
        long createResult = 0;
        if(phone!=null)
        {

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_PHONE_ID,phone.getPhoneId());
            contentValues.put(KEY_NAME,phone.getPhoneName());
            contentValues.put(KEY_PRICE,phone.getPhonePrice());
            contentValues.put(KEY_BRAND,phone.getPhoneBrand());
            contentValues.put(KEY_ERROR,phone.getPhoneErrorInfo());

            contentValues.put(KEY_MARKET_SHARE,phone.getPhoneMarketShare());
            contentValues.put(KEY_RATE,phone.getPhoneRate());
            contentValues.put(KEY_LOVE_COUNT,phone.getPhoneLoveCount());
            contentValues.put(KEY_COMMENT_COUNT,phone.getPhoneCommentCount());

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
    public boolean deleteAllPhones() {
        int doneDelete = 0;
        try {

            Log.i("tag",  "----deleteAllPhones----");
//            mDb.execSQL("delete from "+SQLITE_TABLE);
//            mDb.execSQL("DELETE FROM "+SQLITE_TABLE+" ;");
            doneDelete = mDb.delete(SQLITE_TABLE, null, null);
            Log.i("tag", doneDelete + "--------");
//            Log.w(TAG, Integer.toString(doneDelete));
//            Log.e("doneDelete", doneDelete + "");
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
     * 插入一些手机
     * @param phones
     */
    public void insertSomePhones(final List<Phone> phones) {
        if(phones!=null)
        {
            //子线程中完成批量操作
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Phone phone :
                            phones) {
                        insertPhone(phone);
                    }
                }
            }).start();

        }
    }

    /**
     * 刷新表
     */

    public void updateTable(final List<Phone> phones){
        Log.i("tag",  "----updateTable----");
        //先清空
        deleteAllPhones();
        //再重新插入新数据
        insertSomePhones(phones);
}
    /**
     * 获取热门的手机
     * @param begin 开始位置
     * @param length 获取的数目
     * @param type 按喜爱数(love_count)排序或者按评论数(commet_count)排序 从高到低
     * @return
     */
    public List<Phone> fetchSomeHotPhones(String begin,String length,String type){

    ArrayList<Phone> allphonesList = new ArrayList<Phone>();
    Cursor mCursor = null;
    mCursor=  mDb.query(SQLITE_TABLE,new String[] { KEY_PHONE_ID, KEY_NAME,
            KEY_BRAND, KEY_PRICE,KEY_ERROR,KEY_MARKET_SHARE,KEY_RATE,KEY_LOVE_COUNT,KEY_COMMENT_COUNT, },
            null,null,null,null,type+" desc",begin+","+length

    );
//    mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_PHONE_ID, KEY_NAME,
//            KEY_BRAND, KEY_PRICE,KEY_ERROR,KEY_MARKET_SHARE,KEY_RATE,KEY_LOVE_COUNT,KEY_COMMENT_COUNT, }, null, null, null, null, null);
    if (mCursor.moveToFirst()) {
        do {
            Phone phone = new Phone();
            phone.setPhoneId(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_PHONE_ID)));
            phone.setPhoneName(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_NAME)));
            phone.setPhoneBrand(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_BRAND)));
            phone.setPhonePrice(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_PRICE)));
            phone.setPhoneErrorInfo(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_ERROR)));

            phone.setPhoneMarketShare(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_MARKET_SHARE)));
            phone.setPhoneRate(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_RATE)));
            phone.setPhoneLoveCount(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_LOVE_COUNT)));
            phone.setPhoneCommentCount(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_COMMENT_COUNT)));
            allphonesList.add(phone);
        } while (mCursor.moveToNext());
    }
    if (mCursor != null && !mCursor.isClosed()) {
        mCursor.close();
    }
    return allphonesList;


}
    /**
     * 获取表中的所有手机
     * @return
     */
    public ArrayList<Phone> fetchAll() {

        ArrayList<Phone> allphonesList = new ArrayList<Phone>();
        Cursor mCursor = null;
        mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_PHONE_ID, KEY_NAME,
                KEY_BRAND, KEY_PRICE,KEY_ERROR,KEY_MARKET_SHARE,KEY_RATE,KEY_LOVE_COUNT,KEY_COMMENT_COUNT, }, null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                Phone phone = new Phone();
                    phone.setPhoneId(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_PHONE_ID)));
                    phone.setPhoneName(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_NAME)));
                    phone.setPhoneBrand(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_BRAND)));
                    phone.setPhonePrice(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_PRICE)));
                    phone.setPhoneErrorInfo(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_ERROR)));

                    phone.setPhoneMarketShare(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_MARKET_SHARE)));
                    phone.setPhoneRate(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_RATE)));
                    phone.setPhoneLoveCount(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_LOVE_COUNT)));
                    phone.setPhoneCommentCount(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_COMMENT_COUNT)));
                allphonesList.add(phone);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return allphonesList;
    }

    /**
     * 根据品牌查询手机
     * @param brand
     * @return
     */
    public ArrayList<Phone> getPhoneByBrand(String brand){

        ArrayList<Phone> allphonesList = new ArrayList<Phone>();
        Cursor mCursor = null;
        mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_PHONE_ID, KEY_NAME,
                KEY_BRAND, KEY_PRICE,KEY_ERROR,KEY_MARKET_SHARE,KEY_RATE,KEY_LOVE_COUNT,KEY_COMMENT_COUNT, },

                "phone_brand like?", new String[]{brand}, null, null, null,null);
        if (mCursor.moveToFirst()) {
            do {
                Phone phone = new Phone();
                phone.setPhoneId(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_PHONE_ID)));
                phone.setPhoneName(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_NAME)));
                phone.setPhoneBrand(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_BRAND)));
                phone.setPhonePrice(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_PRICE)));
                phone.setPhoneErrorInfo(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_ERROR)));

                phone.setPhoneMarketShare(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_MARKET_SHARE)));
                phone.setPhoneRate(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_RATE)));
                phone.setPhoneLoveCount(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_LOVE_COUNT)));
                phone.setPhoneCommentCount(mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_COMMENT_COUNT)));
                allphonesList.add(phone);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return allphonesList;

    }

}
