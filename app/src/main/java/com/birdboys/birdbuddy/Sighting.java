package com.birdboys.birdbuddy;

/**
 * Created by Alex on 11/28/17.
 */

public class Sighting {

    private String locName;
    private double lng;
    private double lat;
    private String comName;

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }
}
