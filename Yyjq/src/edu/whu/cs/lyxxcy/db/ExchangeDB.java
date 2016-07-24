package edu.whu.cs.lyxxcy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExchangeDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "exchange";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String INFORMATION = "INFORMATION";
    public static final String KIND = "kind";
    public static final String PRICE = "price";
    public static final String PIC = "pic";
    public static final String PUT_USER_ID = "put_user_id";
    public static final String PUT_USER_NAME = "put_user_name";
    public static final String PUT_USER_PHONE = "put_user_phone";
    public static final String RECEIVE_USER_ID = "receive_user_id";
    public static final String RECEIVE_USER_NAME = "receive_user_name";
    public static final String ASSESS_PTR = "assess_ptr";
    public static final String ASSESS_RTP = "assess_rtp";
    public static final String ASSESS_PTR_SCORE = "assess_ptr_score";
    public static final String ASSESS_RTP_SCORE = "assess_rtp_score";
    public static final String CREATE_TIME = "create_time";
    public static final String END_TIME = "end_time";
    public static final String STATUS = "status"; // 0 1 2 

    public ExchangeDB(Context context) {
        super(context, "exchange", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" 
                + ID + " TEXT PRIMARY KEY," 
                + TITLE + " TEXT NOT NULL,"
                + INFORMATION + " TEXT NOT NULL," 
                + KIND + " TEXT NOT NULL," 
                + PRICE + " INTEGER NOT NULL," 
                + PIC + " TEXT NOT NULL," 
                + PUT_USER_ID + " TEXT NOT NULL,"                
                + PUT_USER_NAME + " TEXT NOT NULL," 
                + PUT_USER_PHONE + " TEXT NOT NULL,"
                + RECEIVE_USER_ID + " TEXT NOT NULL," 
                + RECEIVE_USER_NAME + " TEXT NOT NULL,"
                + ASSESS_PTR + " TEXT NOT NULL," 
                + ASSESS_RTP + " TEXT NOT NULL," 
                + ASSESS_PTR_SCORE + " INTEGER NOT NULL," 
                + ASSESS_RTP_SCORE + " INTEGER NOT NULL," 
                + STATUS + " TEXT NOT NULL," 
                + CREATE_TIME + " TEXT NOT NULL," 
                + END_TIME + " TEXT NOT NULL)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
