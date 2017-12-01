package com.birdboys.birdbuddy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Bird> birdList;
    private List<Sighting> sightingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double min = .99999;
        double max = 1.00002;

        // Add a marker in Sydney and move the camera
        LatLng urhere = new LatLng(43.097348, -73.784180);
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


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(urhere, 14.0f));
    }

    private class FetchBirdTask extends AsyncTask<Void,Void,List<List>> {
        @Override
        protected List<List> doInBackground(Void... params) {
            return new Ebirdr().fetchNewBirds();
        }

        @Override
        protected void onPostExecute(List<List> birdLists) {
            birdList = birdLists.get(0);
            sightingList = birdLists.get(1);
        }
    }
}