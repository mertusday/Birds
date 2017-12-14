package com.birdboys.birdbuddy.sightingDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.birdboys.birdbuddy.Sighting;
import com.birdboys.birdbuddy.sightingDatabase.SightingListDBSchema.SightingListDBTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Andrew Riggs on 12/13/2017.
 */

public class SightingCursorWrapper extends CursorWrapper {
    public SightingCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Sighting getSighting() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(SightingListDBTable.Cols.UUID)));
        String locName = getString(getColumnIndex(SightingListDBTable.Cols.LOCNAME));
        double longitude = getDouble(getColumnIndex(SightingListDBTable.Cols.LONGITUDE));
        double latitude = getDouble(getColumnIndex(SightingListDBTable.Cols.LATITUDE));
        String comName = getString(getColumnIndex(SightingListDBTable.Cols.COMNAME));
        Date date = getDate(getString(getColumnIndex(SightingListDBTable.Cols.DATE)));

        Sighting sighting = new Sighting(uuid);
        sighting.setLocName(locName);
        sighting.setLng(longitude);
        sighting.setLat(latitude);
        sighting.setComName(comName);
        sighting.setDate(date);

        return sighting;
    }

    private Date getDate(String dateString) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
            return date;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
