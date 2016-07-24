package edu.whu.cs.lyxxcy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "user";
    public static final String NAME = "user_name";
    public static final String ID = "_id";
    public static final String SIGN = "sign";

    public UserDB(Context context) {
        super(context, "user", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " TEXT PRIMARY KEY ," + NAME + " TEXT NOT NULL," + SIGN
                + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
