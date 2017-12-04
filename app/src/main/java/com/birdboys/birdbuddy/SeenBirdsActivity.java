package com.birdboys.birdbuddy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SeenBirdsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_birds);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.seen_birds_frag_container);

        if(fragment == null) {
            fragment = new SeenBirdsFragment();
            fragmentManager.beginTransaction().add(R.id.seen_birds_frag_container, fragment)
                    .commit();
        }

    }
}
