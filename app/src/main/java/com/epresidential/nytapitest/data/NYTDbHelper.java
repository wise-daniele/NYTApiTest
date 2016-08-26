package com.epresidential.nytapitest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.epresidential.nytapitest.data.NYTContract.ArticleEntry;
import com.epresidential.nytapitest.model.Article;

/**
 * Created by daniele on 23/08/16.
 */
public class NYTDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "nytimes.db";

    public NYTDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold nyt articles.
        final String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
                ArticleEntry._ID + " INTEGER PRIMARY KEY," +
                ArticleEntry.COLUMN_PUB_DATE + " TEXT NOT NULL, " +
                ArticleEntry.COLUMN_WEB_URL + " TEXT UNIQUE NOT NULL, " +
                ArticleEntry.COLUMN_IMAGE_URL + " TEXT, " +
                ArticleEntry.COLUMN_MAIN_HEADLINE + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}