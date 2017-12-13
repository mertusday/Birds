package com.birdboys.birdbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.util.Arrays;

public class AddBirdActivity extends AppCompatActivity {
    String latitude;
    String longitude;
    String[] birdList = {"Macaw", "Didgerydoo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bird);


        //getActionBar().setTitle("Add a Bird to LifeList");
        //getSupportActionBar().setTitle("Add a Bird to LifeList");


        String[] input = getIntent().getStringArrayExtra("birds");
        latitude = input[0];
        longitude = input[1];
        if(input.length>2) {
            birdList = Arrays.copyOfRange(input, 2, input.length);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, birdList);
        AutoCompleteTextView dropDown = (AutoCompleteTextView) findViewById(R.id.birdName);
        dropDown.setThreshold(1);
        dropDown.setAdapter(adapter);

        EditText lat = (EditText) findViewById(R.id.latDirectAddInput);
        EditText lon = (EditText) findViewById(R.id.longDirectAddInput);

        lat.setText(latitude);
        lon.setText(longitude);
        lat.setEnabled(false);
        lon.setEnabled(false);

        Switch swotch = findViewById(R.id.locationSwtich);
        swotch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton View, boolean checked) {
                EditText lat = (EditText) findViewById(R.id.latDirectAddInput);
                EditText lon = (EditText) findViewById(R.id.longDirectAddInput);
                if(checked){
                    lat.setText(latitude);
                    lon.setText(longitude);
                    lat.setEnabled(false);
                    lon.setEnabled(false);
                }else{
                    lat.setEnabled(true);
                    lon.setEnabled(true);
                }
            }
        });





    }
}
