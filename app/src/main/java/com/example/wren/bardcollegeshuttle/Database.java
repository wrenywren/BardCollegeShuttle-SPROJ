package com.example.wren.bardcollegeshuttle;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/**
 * Created by Wren on 10/23/2016.
 */

public class Database extends SQLiteAssetHelper {

    private static Database mInstance = null;

    private static final String DATABASE_NAMES = "TestDB4.sqlite";

    private static final int DATABASE_VERSION = 1;

    public static Database getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new Database(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public Database(Context context) {
        super(context, DATABASE_NAMES, null, DATABASE_VERSION);
    }
}
