package com.example.fxjzzyo.phome.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ComDB {


        public static final String DATABASE_NAME = "myDatabase"; //数据库名称
    private static final String PHONE_TB_NAME = "PhoneTable";
    private static final String CORE_PARAMATER_TB_NAME = "CoreParamaterTable";
        public static final int DATABASE_VERSION = 1;
        //创建该数据库下手机表的语句
        private static final String CREATE_TABLE_PHONES = " create table  if not exists "
                + PHONE_TB_NAME+" (_id integer primary key autoincrement," +
                "phone_id int, "+
                "phone_name text, " +
                "phone_price text, "+
                "phone_brand text, " +


                "error_info text, " +
                "market_share text, "+


                "rate text, "+
                "love_count int, "+
                "comment_count int"+
                ") ";
               /* "CREATE TABLE if not exists " + StudentDB.SQLITE_TABLE + " (" +
                        StudentDB.KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                        StudentDB.KEY_AGE + "," +
                        StudentDB.KEY_GENDER + "," +
                        StudentDB.KEY_NAME + "," +
                        " UNIQUE (" + StudentDB.KEY_NAME +"));";//暂时规定不能重名*/
        //创建该数据库下核心参数表的语句
        private static final String CREATE_TABLE_CORE_PARAMATER= " create table  if not exists "
                       + CORE_PARAMATER_TB_NAME+" (_id integer primary key autoincrement," +
                       "core_id int, "+
                       "phone_id int, "+
                       "screen_size text, " +
                       "cpu text, "+
                       "ram text, " +


                       "storage_capacity text, " +
                       "back_camera_pixel text, "+
                       "battery_capacity text"+
                       ") ";
                /*"CREATE TABLE if not exists " + TeacherDB.SQLITE_TABLE + " (" +
                        TeacherDB.KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                        TeacherDB.KEY_AGE + "," +
                        TeacherDB.KEY_GENDER + "," +
                        TeacherDB.KEY_NAME + "," +
                        " UNIQUE (" + TeacherDB.KEY_AGE +"));";*/
        private final Context context;
        private DatabaseHelper DBHelper;
        private SQLiteDatabase db;
        /**
         * Constructor
         * @param ctx
         */
        public ComDB(Context ctx)
        {
            this.context = ctx;
            this.DBHelper = new DatabaseHelper(this.context);
        }

        private static class DatabaseHelper extends SQLiteOpenHelper
        {
            DatabaseHelper(Context context)
            {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db)
            {

                db.execSQL(CREATE_TABLE_PHONES);//创建手机表
//                db.execSQL(CREATE_TABLE_CORE_PARAMATER);//创建核心参数表
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion,
                                  int newVersion)
            {
                // Adding any table mods to this guy here
            }


        }

        /**
         * open the db
         * @return this
         * @throws SQLException
         * return type: DBAdapter
         */
        public ComDB open() throws SQLException
        {
            this.db = this.DBHelper.getWritableDatabase();
            return this;
        }

        /**
         * close the db
         * return type: void
         */
        public void close()
        {
            this.DBHelper.close();
        }
}


