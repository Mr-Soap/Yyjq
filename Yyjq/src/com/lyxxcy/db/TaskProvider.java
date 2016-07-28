package com.lyxxcy.db;

import android.content.ContentProvider;
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

public class TaskProvider extends ContentProvider {

    private static final String TAG = "TaskProvider";

    public static final String AUTHORITY = "yyjq.task";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/");

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

    private static final String DATABASE_NAME = "task.db";
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
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + ID + " TEXT PRIMARY KEY," + TASK_NAME
                    + " TEXT NOT NULL," + TASK_SUMMARY + " TEXT NOT NULL," + TASK_DETAIL + " TEXT NOT NULL," + TASK_KIND
                    + " TEXT NOT NULL," + TASK_PAY + " INTEGER NOT NULL," + PUT_USER + " TEXT NOT NULL," + RECEIVE_USER
                    + " TEXT NOT NULL," + ASSESS_PTR + " TEXT NOT NULL," + ASSESS_RTP + " TEXT NOT NULL,"
                    + ASSESS_PTR_SCORE + " INTEGER NOT NULL," + ASSESS_RTP_SCORE + " INTEGER NOT NULL," + PIC
                    + " TEXT NOT NULL," + STATUS + " TEXT NOT NULL," + CREATE_TIME + " TEXT NOT NULL," + END_TIME
                    + " TEXT NOT NULL)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrade database from " + oldVersion + " to " + newVersion);

            // Log.w(TAG, "drop all data!");
            // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            // onCreate(db); ,videoPath TEXT,

        }

    }

}
