package edu.whu.cs.lyxxcy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "task";
    public static final String ID = "_id";
    public static final String TASK_NAME = "task_name";
    public static final String TASK_SUMMARY = "task_summary";
    public static final String TASK_DETAIL = "task_detail";
    public static final String TASK_KIND = "task_kind";
    public static final String TASK_PAY = "task_pay";
    public static final String PUT_USER = "put_user";
    public static final String RECEIVE_USER = "receive_user";
    public static final String ASSESS_PTR = "assess_ptr";
    public static final String ASSESS_RTP = "assess_rtp";
    public static final String ASSESS_PTR_SCORE = "assess_ptr_score";
    public static final String ASSESS_RTP_SCORE = "assess_rtp_score";
    public static final String PIC = "pic";
    public static final String CREATE_TIME = "create_time";
    public static final String END_TIME = "end_time";
    public static final String STATUS = "status"; // 0 1 2 

    public TaskDB(Context context) {
        super(context, "task", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " TEXT PRIMARY KEY," + TASK_NAME + " TEXT NOT NULL,"
                + TASK_SUMMARY + " TEXT NOT NULL," + TASK_DETAIL + " TEXT NOT NULL," + TASK_KIND + " TEXT NOT NULL,"
                + TASK_PAY + " INTEGER NOT NULL," + PUT_USER + " TEXT NOT NULL," + RECEIVE_USER + " TEXT NOT NULL,"
                + ASSESS_PTR + " TEXT NOT NULL," + ASSESS_RTP + " TEXT NOT NULL," + ASSESS_PTR_SCORE
                + " INTEGER NOT NULL," + ASSESS_RTP_SCORE + " INTEGER NOT NULL," + PIC + " TEXT NOT NULL," + STATUS
                + " TEXT NOT NULL," + CREATE_TIME + " TEXT NOT NULL," + END_TIME + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
