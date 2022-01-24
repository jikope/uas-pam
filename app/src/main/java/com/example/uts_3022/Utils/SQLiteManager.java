package com.example.uts_3022.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SQLiteManager extends SQLiteOpenHelper {
    public SQLiteManager(@Nullable Context context) {
        super(context, "MovieDBCache.db", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String createMoviesTable =
                "CREATE TABLE MOVIES (" +
                        "id TEXT," +
                        "title TEXT," +
                        "release_date TEXT," +
                        "original_language TEXT," +
                        "overview TEXT," +
                        "poster_path TEXT" +
                ")";

        sqLiteDatabase.execSQL(createMoviesTable);
    }

    public JSONArray getData() {
        JSONArray data = new JSONArray();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM MOVIES", null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    JSONObject movie = new JSONObject();
                    movie.put("id", cursor.getString(0));
                    movie.put("title", cursor.getString(1));
                    movie.put("release_date", cursor.getString(2));
                    movie.put("original_language", cursor.getString(3));
                    movie.put("overview", cursor.getString(4));
                    movie.put("poster_path", cursor.getString(5));
                    data.put(movie);
                } catch (Exception e) {
                    Log.i("SQLITE", e.getMessage());
                }
            } while (cursor.moveToNext());
        }

        return data;
    }

    public void deleteOldCache() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM MOVIES");
        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String dropMoviesTable =
                "DROP TABLE IF EXISTS MOVIES";

        sqLiteDatabase.execSQL(dropMoviesTable);
        this.onCreate(sqLiteDatabase);
    }

    public void addData(JSONObject movie) {
        SQLiteDatabase sqLiteDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            contentValues.put("id", movie.getString("id"));
            contentValues.put("title", movie.getString("title"));
            contentValues.put("release_date", movie.getString("release_date"));
            contentValues.put("original_language", movie.getString("original_language"));
            contentValues.put("overview", movie.getString("overview"));
            contentValues.put("poster_path", movie.getString("poster_path"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sqLiteDB.insert("MOVIES", null, contentValues);
        sqLiteDB.close();
    }
}
