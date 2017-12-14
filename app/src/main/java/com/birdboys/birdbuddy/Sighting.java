package com.birdboys.birdbuddy;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Alex on 11/28/17.
 */

public class Sighting {

    private UUID uuid;
    private String locName;
    private double lng;
    private double lat;
    private String comName;
    private Date date;

    public Sighting() {
        this.uuid = UUID.randomUUID();
    }

    public Sighting(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
