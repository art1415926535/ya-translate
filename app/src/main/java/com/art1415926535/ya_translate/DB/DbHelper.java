package com.art1415926535.ya_translate.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {
    public static final String TABLE_HISTORY = "history";
    public static final String TABLE_FAVOURITES = "favourites";

    public static final String KEY_ID = "_id";
    public static final String KEY_FROM_LANG_CODE = "from_lang_code";
    public static final String KEY_FROM_TEXT = "from_text";
    public static final String KEY_TO_LANG_CODE = "to_lang_code";
    public static final String KEY_TO_TEXT = "to_text";

    private static final String DATABASE_NAME = "db";
    private static final int DATABASE_VERSION = 1;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String args = " ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_FROM_LANG_CODE + " text,"
                + KEY_FROM_TEXT + " text,"
                + KEY_TO_LANG_CODE + " text,"
                + KEY_TO_TEXT + " text";

        // To speed up the reading divide the data into two tables.
        // No need to do query of two tables. Then why not.
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + args + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAVOURITES + args + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);

        onCreate(db);
    }

}