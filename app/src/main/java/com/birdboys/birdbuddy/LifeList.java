package com.birdboys.birdbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;

import com.birdboys.birdbuddy.birdDatabase.BirdCursorWrapper;
import com.birdboys.birdbuddy.birdDatabase.LifeListDBHelper;
import com.birdboys.birdbuddy.birdDatabase.LifeListDBSchema.LifeListDBTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Riggs on 11/29/2017.
 */

public class LifeList {

    private static LifeList sLifeList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LifeList get(Context context) {
        if(sLifeList == null) {
            sLifeList = new LifeList(context);
        }
        return sLifeList;
    }

    private LifeList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LifeListDBHelper(mContext).getWritableDatabase();
    }

    public void addBird(Bird b) {

        if(getBird(b.getName()) == null) {
            ContentValues values = getContentValues(b);
            mDatabase.insert(LifeListDBTable.NAME, null, values);
        }
    }

    public void updateSeen(Bird bird) {
        if (birdSeen(bird)) {
            bird.setSeen(true);
        }
    }

    public boolean birdSeen(Bird bird) {
        if(getBird(bird.getName()) != null) {
            return true;
        } else {
            return false;
        }
    }

    public List<Bird> getBirds() {
        List<Bird> birds = new ArrayList<>();

        BirdCursorWrapper cursorWrapper = queryBirds(null, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                birds.add(cursorWrapper.getBird());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return birds;
    }

    public Bird getBird(String name) {
        BirdCursorWrapper cursorWrapper = queryBirds(
                LifeListDBTable.Cols.BIRDNAME + " = ?",
                new String[] {name}
        );

        try {
            if(cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getBird();
        } finally {
            cursorWrapper.close();
        }
    }

    public void updateBird(Bird bird) {
        String birdNameString = bird.getName();
        ContentValues values = getContentValues(bird);

        mDatabase.update(LifeListDBTable.NAME, values, LifeListDBTable.Cols.BIRDNAME +
                " = ?", new String[] {birdNameString});
    }

    private BirdCursorWrapper queryBirds(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                LifeListDBTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new BirdCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Bird bird) {
        ContentValues values = new ContentValues();
        values.put(LifeListDBTable.Cols.BIRDNAME, bird.getName());
        values.put(LifeListDBTable.Cols.BIRDURLNAME, bird.getUri_name());
        values.put(LifeListDBTable.Cols.SEEN, bird.isSeen() ? 1 : 0);

        return values;
    }
}
