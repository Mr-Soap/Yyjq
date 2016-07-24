package edu.whu.cs.lyxxcy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "user"; // ±íÃû
    public static final String ID = "_id";
    public static final String USER_NAME = "user_name"; // 
    public static final String PET_NAME = "pet_name"; //
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String SEX = "sex";
    public static final String STUDENT_ID = "student_id";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String CREDIT_LEVEL_EXCHANGE = "credit_level_exchange";
    public static final String CREDIT_LEVEL_TASK = "credit_level_task";
    public static final String CREDIT_POINTS_EXCHANGE = "credit_points_exchange";
    public static final String CREDIT_POINTS_TASK = "credit_points_task";
    public static final String CREATE_TIME = "create_tim";
    public static final String IMAGE_LOCATION = "image_location";
    public static final String SIGN = "sign";

    public UserDB(Context context) {
        super(context, "user", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " TEXT PRIMARY KEY," + USER_NAME + " TEXT NOT NULL,"
                + PET_NAME + " TEXT NOT NULL," + ACCOUNT + " TEXT NOT NULL," + PASSWORD + " TEXT NOT NULL," + SEX
                + " INTEGER NOT NULL," + STUDENT_ID + " TEXT NOT NULL," + EMAIL + " TEXT NOT NULL," + PHONE
                + " TEXT NOT NULL," + CREDIT_LEVEL_EXCHANGE + " INTEGER NOT NULL," + CREDIT_LEVEL_TASK
                + " INTEGER NOT NULL," + CREDIT_POINTS_EXCHANGE + " INTEGER NOT NULL," + CREDIT_POINTS_TASK
                + " INTEGER NOT NULL," + CREATE_TIME + " TEXT NOT NULL," + IMAGE_LOCATION + " TEXT NOT NULL," + SIGN
                + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
