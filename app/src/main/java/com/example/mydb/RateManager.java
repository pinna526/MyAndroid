package com.example.mydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RateManager {
    private  DBHelper dbHelper;
    private  String TBNAME;

    public RateManager(Context context){
        dbHelper = new DBHelper(context);
        TBNAME = dbHelper.TB_NAME;
    }

    //添加数据
    public void add(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name",item.getName());
        values.put("rate",item.getRate());

        db.insert(TBNAME,null,values);
        db.close();

    }

    //查找数据
    public RateItem findById(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                TBNAME,
                null,
                "ID=?",
                new String[]{String.valueOf(id)},
                null,null,null);

        RateItem item = null;
        if (cursor!=null&&cursor.moveToFirst()) {
            item = new RateItem();
            item.setId(cursor.getString(cursor.getColumnIndex("NAME")));
            item.setName(cursor.getString(cursor.getColumnIndex("RATE")));
            cursor.close();
        }
        db.close();
        return item;
    }



}
