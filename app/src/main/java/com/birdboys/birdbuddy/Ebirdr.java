package com.birdboys.birdbuddy;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import im.delight.android.location.SimpleLocation;

/**
 * Created by Alex on 11/28/17.
 */

public class Ebirdr {

    private static final String TAG = "ebirdr";
    private String latitude;
    private String longitude;

    public Ebirdr(String[] LongLat){
        longitude = LongLat[0];
        latitude = LongLat[1];
    }


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public void parseBirds(List<Sighting> sightings, List<Bird> birds, JSONArray jsonArray) throws IOException, JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject sightingObject = jsonArray.getJSONObject(i);
            Sighting sighting = new Sighting();
            sighting.setLat(sightingObject.getDouble("lat"));
            sighting.setLng(sightingObject.getDouble("lng"));
            sighting.setLocName(sightingObject.getString("locName"));
            String comName = sightingObject.getString("comName");
            sighting.setComName(comName);
            if (!hasBird(birds, comName)) {
                birds.add(new Bird(comName));
            }
            sightings.add(sighting);
        }
    }

    private boolean hasBird(List<Bird> list, String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String (getUrlBytes(urlSpec));
    }

    public List<List> fetchNewBirds() {

        List<List> birdLists = new ArrayList<>();
        List<Bird> birds = new ArrayList<>();
        List<Sighting> sightings = new ArrayList<>();

        try {
            String url = Uri.parse("http://ebird.org/ws1.1/data/obs/geo/recent?lng=-73.78&lat=43.10&fmt=json").buildUpon().build().toString();

         //   String url = Uri.parse("http://ebird.org/ws1.1/data/obs/geo/recent?lng="+longitude+"&lat="+latitude+"&fmt=json").buildUpon().build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);
            parseBirds(sightings, birds, jsonArray);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        birdLists.add(0, birds);
        birdLists.add(1, sightings);

        return birdLists;
    }

}
