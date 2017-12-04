package com.birdboys.birdbuddy.birdDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.birdboys.birdbuddy.birdDatabase.LifeListDBSchema.LifeListDBTable;

/**
 * Created by Andrew Riggs on 11/29/2017.
 */

public class LifeListDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "lifeList.db";

    public LifeListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LifeListDBTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                LifeListDBTable.Cols.BIRDNAME + ", " +
                LifeListDBTable.Cols.BIRDURLNAME + ", " +
                LifeListDBTable.Cols.SEEN +")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
