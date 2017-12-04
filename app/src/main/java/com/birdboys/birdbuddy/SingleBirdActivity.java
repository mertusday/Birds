package com.birdboys.birdbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class SingleBirdActivity extends AppCompatActivity {

    private static final String EXTRA_BIRD_NAME = "com.skidmore.ariggs.todolist.bird_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bird);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.single_bird_frag_container);

        if (fragment == null) {
            String birdName = (String) getIntent().getSerializableExtra(EXTRA_BIRD_NAME);
            fragment = SingleBirdFragment.newInstance(birdName);
            fragmentManager.beginTransaction().add(R.id.single_bird_frag_container,
                    fragment).commit();
        }
    }

    public static Intent newIntent(Context packageContext, String birdName) {
        Intent intent = new Intent(packageContext, SingleBirdActivity.class);
        intent.putExtra(EXTRA_BIRD_NAME, birdName);
        return intent;
    }
}
