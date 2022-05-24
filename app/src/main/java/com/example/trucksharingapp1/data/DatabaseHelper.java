package com.example.trucksharingapp1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.trucksharingapp1.model.User;
import com.example.trucksharingapp1.util.Util;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + String.valueOf(Util.USER_ID) + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                        + Util.FULLNAME + " TEXT," + Util.USERNAME + " TEXT," + Util.PASSWORD + " TEXT," + Util.PHONE + " TEXT," + Util.IMAGE + " BLOB)";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String DROP_USER_TABLE = "DROP TABLE IF EXISTS ";
        sqLiteDatabase.execSQL(DROP_USER_TABLE + Util.TABLE_NAME + ";");

        onCreate(sqLiteDatabase);
    }

    public long insertUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.FULLNAME, user.getFullname());
        contentValues.put(Util.USERNAME, user.getUsername());
        contentValues.put(Util.PASSWORD, user.getPassword());
        contentValues.put(Util.PHONE, user.getPhone());
        contentValues.put(Util.IMAGE, user.getByteArray());
        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    public boolean fetchUser (String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.USER_ID}, Util.USERNAME + "=? and " + Util.PASSWORD + "=?" , new String[] {username, password}, null, null, null);
        int numberOfRows = cursor.getCount();
        db.close();

        if (numberOfRows > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public byte[] fetchImage (String username) {
        byte[] image;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, null, Util.USERNAME + "=?" , new String[] {username}, null, null, null);
        cursor.moveToFirst();
        image = cursor.getBlob(cursor.getColumnIndex(Util.IMAGE));
        db.close();
        return image;
    }

    public String fetchfullname (String username) {
        String fullname;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, null, Util.USERNAME + "=?" , new String[] {username}, null, null, null);
        cursor.moveToFirst();
        fullname = cursor.getString(cursor.getColumnIndex(Util.FULLNAME));
        db.close();
        return fullname;
    }

    public String fetchphone (String username) {
        String phonenum;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, null, Util.USERNAME + "=?" , new String[] {username}, null, null, null);
        cursor.moveToFirst();
        phonenum = cursor.getString(cursor.getColumnIndex(Util.PHONE));
        db.close();
        return phonenum;
    }
}
