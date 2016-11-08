package com.example.wren.bardcollegeshuttle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Wren on 10/23/2016.
 */

public class DbObject {
    public static Database dbHelper;
    private SQLiteDatabase db;

    public DbObject(Context context) {
        dbHelper = new Database(context);
        this.db = dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getDbConnection(){
        return this.db;
    }

    public void closeDbConnection(){
        if(this.db != null){
            this.db.close();
        }
    }

}
