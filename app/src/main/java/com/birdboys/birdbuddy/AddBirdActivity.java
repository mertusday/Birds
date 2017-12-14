package com.birdboys.birdbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

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

        Date enter = new Date();
        String date = new SimpleDateFormat("dd/MM/yyyy").format(enter);

        Log.i("date", date);
        EditText dateText = (EditText) findViewById(R.id.dateInput);
        dateText.setEnabled(false);
        dateText.setText(date);

        Button addButton = (Button) findViewById(R.id.submitAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sighting addBird = new Sighting();
                AutoCompleteTextView dropDown = (AutoCompleteTextView) findViewById(R.id.birdName);
                EditText lat = (EditText) findViewById(R.id.latDirectAddInput);
                EditText lon = (EditText) findViewById(R.id.longDirectAddInput);
                EditText dateText = (EditText) findViewById(R.id.dateInput);

                addBird.setComName(dropDown.getText().toString());
                addBird.setLat(Double.parseDouble(lat.getText().toString()));
                addBird.setLng(Double.parseDouble(lon.getText().toString()));
                try {
                    addBird.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(dateText.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                LifeList.get(AddBirdActivity.this).addSighting(addBird);
                Intent intent = new Intent(AddBirdActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }
}
