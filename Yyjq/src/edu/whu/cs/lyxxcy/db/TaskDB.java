package edu.whu.cs.lyxxcy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "task";
    public static final String ID = "_id";
    public static final String NAME = "task_name";
    public static final String PUT_USER = "put_user";
    public static final String RECEIVE_USER = "receive_user";
    public static final String CREATE_TIME = "create_time";

    public TaskDB(Context context) {
        super(context, "user", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " TEXT PRIMARY KEY ," + NAME + " TEXT NOT NULL," + PUT_USER
                + " TEXT NOT NULL," + RECEIVE_USER + " TEXT NOT NULL," + CREATE_TIME + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
