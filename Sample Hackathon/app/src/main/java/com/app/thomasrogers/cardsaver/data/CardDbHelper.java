package com.app.thomasrogers.cardsaver.data;

/**
 * Created by thomasrogers on 1/1/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "cardsDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 10;

    // Constructor
    CardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + CardContract.CardEntry.TABLE_NAME + " (" +
                CardContract.CardEntry._ID + " INTEGER PRIMARY KEY, " +
                CardContract.CardEntry.COLUMN_IMAGE + " BLOB NOT NULL, " +
                CardContract.CardEntry.COLUMN_VCARD_JSON + " TEXT NULL, " +
                CardContract.CardEntry.COLUMN_ALL_VALUES + " TEXT NULL, " +
                CardContract.CardEntry.COLUMN_DATE_CREATED + " REAL NOT NULL, " +
                CardContract.CardEntry.COLUMN_LAST_NAME + " TEXT NULL, " +
                CardContract.CardEntry.COLUMN_COMPANY_NAME + " TEXT NULL);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CardContract.CardEntry.TABLE_NAME);
        onCreate(db);
    }
}
