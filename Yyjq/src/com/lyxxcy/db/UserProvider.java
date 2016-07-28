package com.lyxxcy.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class UserProvider extends ContentProvider {

    private static final String TAG = "UserProvider";

    public static final String AUTHORITY = "yyjq.user";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/");

    public static final String TABLE_NAME = "user"; // ±íÃû
    public static final String ID = "_id";
    public static final String USER_NAME = "user_name"; // 
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

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        mOpenHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        Log.i(TAG, "getType---uri=" + uri);
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        /* now insert into database */
        long rowId = db.insert(TABLE_NAME, null, values);
        if (rowId > 0) {
            uri = ContentUris.withAppendedId(uri, rowId);
            sendNotify(uri);
            return uri;
        }
        throw new SQLException("Failed to insert row into: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, selection, selectionArgs);
        if (count > 0) {
            sendNotify(uri);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = db.update(TABLE_NAME, values, selection, selectionArgs);

        sendNotify(uri);

        return count;
    }

    private void sendNotify(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);

    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + ID + " LONG PRIMARY KEY," + USER_NAME
                    + " TEXT NOT NULL," + PASSWORD + " TEXT NOT NULL," + SEX + " INTEGER NOT NULL," + STUDENT_ID
                    + " TEXT NOT NULL," + EMAIL + " TEXT ," + PHONE + " TEXT NOT NULL," + CREDIT_LEVEL_EXCHANGE
                    + " INTEGER NOT NULL," + CREDIT_LEVEL_TASK + " INTEGER NOT NULL," + CREDIT_POINTS_EXCHANGE
                    + " INTEGER NOT NULL," + CREDIT_POINTS_TASK + " INTEGER NOT NULL," + CREATE_TIME + " TEXT NOT NULL,"
                    + IMAGE_LOCATION + " TEXT ," + SIGN + " TEXT NOT NULL)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrade database from " + oldVersion + " to " + newVersion);

            // Log.w(TAG, "drop all data!");
            // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            // onCreate(db); ,videoPath TEXT,

        }

        //        public static void deleteUser(Context context, String username) {
        //
        //            ContentResolver cr = context.getContentResolver();
        //            cr.delete(CONTENT_URI, "name=?", new String[] { username });
        //
        //            return;
        //
        //        }
        public static void changePassword(Context context, String username, String password) {

            ContentValues values = new ContentValues();
            values.put(PASSWORD, password);

            ContentResolver cr = context.getContentResolver();
            int count = cr.update(CONTENT_URI, values, "name=?", new String[] { username });
            return;

        }

    }

    public static void addUser(Context context, long i_id, String i_name, String i_sex, String i_email, String i_phone,
            String i_student_id, String i_password_again, String i_sign, String i_time) {
        // TODO Auto-generated method stub
        ContentResolver cr = context.getContentResolver();

        /* insert new */
        ContentValues cv = new ContentValues();

        cv.put(ID, i_id);
        cv.put(USER_NAME, i_name);
        cv.put(SEX, i_sex);
        cv.put(EMAIL, i_email);
        cv.put(PHONE, i_phone);
        cv.put(PASSWORD, i_password_again);
        cv.put(SIGN, i_sign);
        cv.put(STUDENT_ID, i_student_id);
        cv.put(CREDIT_LEVEL_EXCHANGE, 0);
        cv.put(CREDIT_LEVEL_TASK, 0);
        cv.put(CREDIT_POINTS_EXCHANGE, 0);
        cv.put(CREDIT_POINTS_TASK, 0);
        cv.put(CREATE_TIME, i_time);
        cr.insert(CONTENT_URI, cv);

        return;

    }

    public static UserInfo getUserFromName(Context context, String username) {
        // TODO Auto-generated method stub
        UserInfo user = null;

        Cursor cursor = context.getContentResolver().query(CONTENT_URI,
                new String[] { ID + " as _id", USER_NAME, PASSWORD }, "USER_NAME=?", new String[] { username }, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new UserInfo();

                user.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));

            }
            cursor.close();
        }

        return user;
    }

}
