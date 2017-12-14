package com.birdboys.birdbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.birdboys.birdbuddy.birdDatabase.BirdCursorWrapper;
import com.birdboys.birdbuddy.birdDatabase.LifeListDBHelper;
import com.birdboys.birdbuddy.birdDatabase.LifeListDBSchema.LifeListDBTable;
import com.birdboys.birdbuddy.sightingDatabase.SightingCursorWrapper;
import com.birdboys.birdbuddy.sightingDatabase.SightingListDBHelper;
import com.birdboys.birdbuddy.sightingDatabase.SightingListDBSchema;
import com.birdboys.birdbuddy.sightingDatabase.SightingListDBSchema.SightingListDBTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Andrew Riggs on 11/29/2017.
 */

public class LifeList {

    private static LifeList sLifeList;

    private Context mContext;
    private SQLiteDatabase mBirdDatabase;
    private SQLiteDatabase mSightingDatabase;

    public static LifeList get(Context context) {
        if(sLifeList == null) {
            sLifeList = new LifeList(context);
        }
        return sLifeList;
    }

    private LifeList(Context context) {
        mContext = context.getApplicationContext();
        mBirdDatabase = new LifeListDBHelper(mContext).getWritableDatabase();
        mSightingDatabase = new SightingListDBHelper(mContext).getWritableDatabase();
    }

    public void addBird(Bird b) {
        if(getBird(b.getName()) == null) {
            ContentValues values = getBirdContentValues(b);
            mBirdDatabase.insert(LifeListDBTable.NAME, null, values);
        }
    }

    public void addSighting(Sighting s) {
        ContentValues values = getSightingContentValues(s);
        mSightingDatabase.insert(SightingListDBTable.NAME, null, values);
        Bird bird = new Bird(s.getComName());
        addBird(bird);
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

    public List<Sighting> getSightings() {
        List<Sighting> sightings = new ArrayList<>();

        SightingCursorWrapper cursorWrapper = querySightings(null, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                sightings.add(cursorWrapper.getSighting());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return sightings;
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

    public Sighting getSighting(UUID uuid) {
        String uuidString = uuid.toString();
        SightingCursorWrapper cursorWrapper = querySightings(
                SightingListDBTable.Cols.UUID + " = ?",
                new String[] {uuidString}
        );

        try {
            if(cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getSighting();
        } finally {
            cursorWrapper.close();
        }
    }

    public void updateBird(Bird bird) {
        String birdNameString = bird.getName();
        ContentValues values = getBirdContentValues(bird);

        mBirdDatabase.update(LifeListDBTable.NAME, values, LifeListDBTable.Cols.BIRDNAME +
                " = ?", new String[] {birdNameString});
    }

    public void updateSighting(Sighting sighting) {
        String sightingUUID = sighting.getUuid().toString();
        ContentValues values = getSightingContentValues(sighting);

        mSightingDatabase.update(SightingListDBTable.NAME, values, SightingListDBTable.Cols.UUID +
                " = ?", new String[] {sightingUUID});
    }

    private BirdCursorWrapper queryBirds(String whereClause, String[] whereArgs) {
        Cursor cursor = mBirdDatabase.query(
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

    private SightingCursorWrapper querySightings(String whereClause, String[] whereArgs) {
        Cursor cursor = mSightingDatabase.query(
                SightingListDBSchema.SightingListDBTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new SightingCursorWrapper(cursor);
    }

    private static ContentValues getBirdContentValues(Bird bird) {
        ContentValues values = new ContentValues();
        values.put(LifeListDBTable.Cols.BIRDNAME, bird.getName());
        values.put(LifeListDBTable.Cols.BIRDURLNAME, bird.getUri_name());
        values.put(LifeListDBTable.Cols.SEEN, bird.isSeen() ? 1 : 0);

        return values;
    }

    private static ContentValues getSightingContentValues(Sighting sighting) {
        ContentValues values = new ContentValues();
        values.put(SightingListDBTable.Cols.UUID, sighting.getUuid().toString());
        values.put(SightingListDBTable.Cols.LOCNAME, sighting.getLocName());
        values.put(SightingListDBTable.Cols.LONGITUDE, sighting.getLng());
        values.put(SightingListDBTable.Cols.LATITUDE, sighting.getLat());
        values.put(SightingListDBTable.Cols.COMNAME, sighting.getComName());
        values.put(SightingListDBTable.Cols.DATE, new SimpleDateFormat("dd/MM/yyyy")
                .format(sighting.getDate()));

        return values;
    }
}
