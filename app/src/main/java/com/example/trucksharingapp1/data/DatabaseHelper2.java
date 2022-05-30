package com.example.trucksharingapp1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.trucksharingapp1.model.Order;
import com.example.trucksharingapp1.model.User;
import com.example.trucksharingapp1.util.Util;

import java.util.ArrayList;
import java.util.List;


//Database use to store the order information
public class DatabaseHelper2 extends SQLiteOpenHelper {

    public DatabaseHelper2(@Nullable Context context) {
        super(context, Util.DATABASE_NAME_2, null, Util.DATABASE_VERSION_2);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME_2 + "(" + String.valueOf(Util.ORDER_ID) + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Util.USERNAME + " TEXT," + Util.RECEIVERNAME + " TEXT," + Util.DATE + " TEXT," + Util.TIME + " TEXT," + Util.LOCATION + " TEXT,"
                + Util.GOOD_TYPE + " TEXT," + Util.WEIGHT + " TEXT," + Util.WIDTH + " TEXT," + Util.LENGTH + " TEXT," + Util.HEIGHT + " TEXT," + Util.VEHICLE_TYPE + " TEXT," + Util.IMAGE + " BLOB)";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String DROP_USER_TABLE = "DROP TABLE IF EXISTS ";
        sqLiteDatabase.execSQL(DROP_USER_TABLE + Util.TABLE_NAME_2 + ";");

        onCreate(sqLiteDatabase);
    }

    public long insertOrder (Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USERNAME, order.getUsername());
        contentValues.put(Util.RECEIVERNAME, order.getReceivername());
        contentValues.put(Util.DATE, order.getDate());
        contentValues.put(Util.TIME, order.getTime());
        contentValues.put(Util.LOCATION, order.getLocation());
        contentValues.put(Util.GOOD_TYPE, order.getGoodtype());
        contentValues.put(Util.WEIGHT, order.getWeight());
        contentValues.put(Util.WIDTH, order.getWidth());
        contentValues.put(Util.LENGTH, order.getLength());
        contentValues.put(Util.HEIGHT, order.getHeight());
        contentValues.put(Util.VEHICLE_TYPE, order.getVehicletype());
        contentValues.put(Util.IMAGE, order.getImage());
        long newRowId = db.insert(Util.TABLE_NAME_2, null, contentValues);
        db.close();
        return newRowId;
    }

    //Fetch all orders in database
    public List<Order> fetchAllOrders () {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_2;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrder_id(cursor.getInt(0));
                order.setUsername(cursor.getString(1));
                order.setReceivername(cursor.getString(2));
                order.setDate(cursor.getString(3));
                order.setTime(cursor.getString(4));
                order.setLocation(cursor.getString(5));
                order.setGoodtype(cursor.getString(6));
                order.setWeight(cursor.getString(7));
                order.setWidth(cursor.getString(8));
                order.setLength(cursor.getString(9));
                order.setHeight(cursor.getString(10));
                order.setVehicletype(cursor.getString(11));
                order.setImage(cursor.getBlob(12));
                orderList.add(order);

            } while (cursor.moveToNext());
        }
        return orderList;
    }

    //Fetch only the orders using the username
    public List<Order> fetchSelectedOrders (String username) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME_2, null, Util.USERNAME + "=?" , new String[] {username}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrder_id(cursor.getInt(0));
                order.setUsername(cursor.getString(1));
                order.setReceivername(cursor.getString(2));
                order.setDate(cursor.getString(3));
                order.setTime(cursor.getString(4));
                order.setLocation(cursor.getString(5));
                order.setGoodtype(cursor.getString(6));
                order.setWeight(cursor.getString(7));
                order.setWidth(cursor.getString(8));
                order.setLength(cursor.getString(9));
                order.setHeight(cursor.getString(10));
                order.setVehicletype(cursor.getString(11));
                order.setImage(cursor.getBlob(12));
                orderList.add(order);

            } while (cursor.moveToNext());
        }
        return orderList;
    }

}
