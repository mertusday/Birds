package com.birdboys.birdbuddy;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class BirdsNearbyActivity extends AppCompatActivity {

    private static final String EXTRA_LAT = "com.birdboys.birdbuddy.lat";
    private static final String EXTRA_LON = "com.birdboys.birdbuddy.lon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birds_nearby);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.nearby_frag_container);

        if(fragment == null) {
            Double lat = (Double) getIntent().getSerializableExtra(EXTRA_LAT);
            Double lon = (Double) getIntent().getSerializableExtra(EXTRA_LON);
            fragment = BirdsNearbyFragment.newInstance(lat, lon);
            fragmentManager.beginTransaction().add(R.id.nearby_frag_container, fragment).commit();
        }
    }

    public static Intent newIntent(Context packageContext, Double lat, Double lon) {
        Intent intent = new Intent(packageContext, BirdsNearbyActivity.class);
        intent.putExtra(EXTRA_LAT, lat);
        intent.putExtra(EXTRA_LON, lon);
        return intent;
    }
}
