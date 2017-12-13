package com.birdboys.birdbuddy.sightingDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.birdboys.birdbuddy.sightingDatabase.SightingListDBSchema.SightingListDBTable;

/**
 * Created by Andrew Riggs on 12/13/2017.
 */

public class SightingListDBHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "sightingList.db";

    public SightingListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SightingListDBTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                SightingListDBTable.Cols.UUID + ", " +
                SightingListDBTable.Cols.LOCNAME + ", " +
                SightingListDBTable.Cols.LATITUDE + ", " +
                SightingListDBTable.Cols.LONGITUDE + ", " +
                SightingListDBTable.Cols.COMNAME + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
