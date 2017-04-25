package com.art1415926535.ya_translate.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.art1415926535.ya_translate.models.Phrase;

import java.util.ArrayList;
import java.util.List;


public class DataBase {

    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String table;
    private String[] allColumns = {
            DBHelper.KEY_ID,
            DBHelper.KEY_FROM_LANG_CODE,
            DBHelper.KEY_FROM_TEXT,
            DBHelper.KEY_TO_LANG_CODE,
            DBHelper.KEY_TO_TEXT
    };

    public DataBase(Context context, String tableName) {
        dbHelper = new DBHelper(context);
        table = tableName;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createPhrase(Phrase phrase) {
        ContentValues values = phrase.toContentValues();
        values.remove(DBHelper.KEY_ID);
        return database.insert(table, null, values);
    }

    public void updatePhrase(Phrase phrase){
        ContentValues cv = phrase.toContentValues();
        String id = cv.getAsString(DBHelper.KEY_ID);
        cv.remove(DBHelper.KEY_ID);
        database.update(table, cv, DBHelper.KEY_ID + "=?", new String[] { id });
    }

    public void deletePhrase(Phrase phrase) {
        String whereClause = DBHelper.KEY_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(phrase.getId()) };

        database.delete(table, whereClause, whereArgs);
    }

    public List<Phrase> getAllPhrases() {
        List<Phrase> comments = new ArrayList<>();

        Cursor cursor = database.query(table, allColumns, null, null, null, null, DBHelper.KEY_ID);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Phrase phrase = cursorToComment(cursor);
            comments.add(phrase);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Phrase cursorToComment(Cursor cursor) {
        Phrase phrase = new Phrase();

        phrase.setId(cursor.getLong(0));
        phrase.setFromLangCode(cursor.getString(1));
        phrase.setFromText(cursor.getString(2));
        phrase.setToLangCode(cursor.getString(3));
        phrase.setToText(cursor.getString(4));

        return phrase;
    }
}
