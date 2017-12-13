package com.birdboys.birdbuddy.sightingDatabase;

/**
 * Created by Andrew Riggs on 12/13/2017.
 */

public class SightingListDBSchema {
    public static final class SightingListDBTable {
        public static final String NAME = "sightinglist";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String LOCNAME = "locname";
            public static final String LONGITUDE = "longitude";
            public static final String LATITUDE = "latitude";
            public static final String COMNAME = "comname";
        }
    }
}
