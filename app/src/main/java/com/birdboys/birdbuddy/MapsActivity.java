package com.birdboys.birdbuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import im.delight.android.location.SimpleLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Bird> birdList;
    private List<Sighting> sightingList;
    private SimpleLocation sLocation;
    private double latitude;
    private double longitude;
    private static String[] LOCATION = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        System.out.println("Made 0");

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, LOCATION, 1);
            return;
        }
        sLocation = new SimpleLocation(this);

        if (!sLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        sLocation.beginUpdates();

        latitude = sLocation.getLatitude();
        longitude = sLocation.getLongitude();

        sLocation.endUpdates();

        Log.i("tag",latitude+"   -    "+longitude);

        try {
            new FetchBirdTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //System.out.println(birdList.get(0).getName().toString());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button localButton = (Button) findViewById(R.id.LocalButton);
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*
        LatLng test1 = new LatLng(43.0961323, -73.7844899);
        LatLng test2 = new LatLng(43.0946822, -73.767457);
        double jitterLng = 43.0946822 *(Math.random()*(max-min)+min);
        double jitterLat = -73.767457 *(Math.random()*(max-min)+min);

        System.out.print("43.0946822 vs "+jitterLng);
        System.out.print("-73.767457 vs "+jitterLat);


        LatLng test3 = new LatLng(jitterLng, jitterLat);

        //need to add jitter to each value or they will stay on top of one another

        mMap.addMarker(new MarkerOptions().position(test1).title("Black-capped Chickadee"));
        mMap.addMarker(new MarkerOptions().position(test2).title("American Crow"));
        mMap.addMarker(new MarkerOptions().position(test3).title("Blue Jay"));


        */
        LatLng urhere = new LatLng(latitude, longitude);

        //LatLng urhere = new LatLng(latitude,longitude);


        double min = .99999;
        double max = 1.00002;
        for(int i=0; i<sightingList.size(); i++){
            double latTemp = sightingList.get(i).getLat();
            double lonTemp = sightingList.get(i).getLng();

            Log.i("Loop","here"+i+" lat "+latTemp+ " long "+lonTemp);
            latTemp = latTemp *(Math.random()*(max-min)+min);
            lonTemp = lonTemp *(Math.random()*(max-min)+min);

            LatLng temp = new LatLng(latTemp, lonTemp);
            mMap.addMarker(new MarkerOptions().position(temp).title(sightingList.get(i).getComName()));

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(urhere, 14.0f));
    }

    private class FetchBirdTask extends AsyncTask<Void,Void,List<List>> {
        @Override
        protected List<List> doInBackground(Void... params) {
            String[] LongLat = {""+longitude,""+latitude};
            List<List> birdLists = new Ebirdr(LongLat).fetchNewBirds();
            birdList = birdLists.get(0);
            sightingList = birdLists.get(1);
            return birdLists;
        }

        /*
        @Override
        protected void onPostExecute(List<List> birdLists) {


            Log.i("MainMap","finished bird list: "+birdLists.get(0).get(0).getClass());
        }
        */
    }





}