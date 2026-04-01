package com.example.sqlitelab;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sewing_company.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "orders";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ORDER_NUMBER = "order_number";
    public static final String COLUMN_CLIENT_NAME = "client_name";
    public static final String COLUMN_RECEPTION_DATE = "reception_date";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_STATUS = "status";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ORDER_NUMBER + " TEXT NOT NULL, "
            + COLUMN_CLIENT_NAME + " TEXT NOT NULL, "
            + COLUMN_RECEPTION_DATE + " TEXT NOT NULL, "
            + COLUMN_COST + " REAL NOT NULL, "
            + COLUMN_STATUS + " TEXT NOT NULL);";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addOrder(String orderNumber, String clientName, String receptionDate, double cost, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_NUMBER, orderNumber);
        values.put(COLUMN_CLIENT_NAME, clientName);
        values.put(COLUMN_RECEPTION_DATE, receptionDate);
        values.put(COLUMN_COST, cost);
        values.put(COLUMN_STATUS, status);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String orderNumber = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_NUMBER));
                String clientName = cursor.getString(cursor.getColumnIndex(COLUMN_CLIENT_NAME));
                String receptionDate = cursor.getString(cursor.getColumnIndex(COLUMN_RECEPTION_DATE));
                double cost = cursor.getDouble(cursor.getColumnIndex(COLUMN_COST));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
                orderList.add(new Order(id, orderNumber, clientName, receptionDate, cost, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public int updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_NUMBER, order.getOrderNumber());
        values.put(COLUMN_CLIENT_NAME, order.getClientName());
        values.put(COLUMN_RECEPTION_DATE, order.getReceptionDate());
        values.put(COLUMN_COST, order.getCost());
        values.put(COLUMN_STATUS, order.getStatus());
        int rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        db.close();
        return rows;
    }

    public void deleteOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}