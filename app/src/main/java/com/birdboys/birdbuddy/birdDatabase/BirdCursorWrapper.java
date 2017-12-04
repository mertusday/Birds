package com.birdboys.birdbuddy.birdDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.birdboys.birdbuddy.Bird;
import com.birdboys.birdbuddy.birdDatabase.LifeListDBSchema.LifeListDBTable;

/**
 * Created by Andrew Riggs on 12/4/2017.
 */

public class BirdCursorWrapper extends CursorWrapper {
    public BirdCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Bird getBird() {
        String birdName = getString(getColumnIndex(LifeListDBTable.Cols.BIRDNAME));
        String birdUrl = getString(getColumnIndex(LifeListDBTable.Cols.BIRDURLNAME));
        int seen = getInt(getColumnIndex(LifeListDBTable.Cols.SEEN));

        Bird bird = new Bird(birdName);
        bird.setSeen(seen != 0);

        return bird;
    }
}
