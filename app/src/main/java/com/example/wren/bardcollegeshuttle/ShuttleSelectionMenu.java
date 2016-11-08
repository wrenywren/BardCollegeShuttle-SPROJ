package com.example.wren.bardcollegeshuttle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShuttleSelectionMenu extends AppCompatActivity {
    private Button mCampusButton;
    private Button mAreaButton;
    private Button mTrainButton;
    private Button mAirportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Bard College Shuttle");
        setContentView(R.layout.activity_shuttle_selection_menu);

        mCampusButton = (Button) findViewById(R.id.campus_shuttle_button);
        mCampusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Start Campus Shuttle Activity
                Intent i = new Intent(ShuttleSelectionMenu.this, CampusShuttle.class);
                startActivity(i);
            }
        });

        mAreaButton = (Button) findViewById(R.id.area_shuttle_button);
        mAreaButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Start Area Shuttle Activity
                Intent i = new Intent(ShuttleSelectionMenu.this, AreaShuttle.class);
                startActivity(i);
            }
        });

        mTrainButton = (Button) findViewById(R.id.train_shuttle_button);
        mTrainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Start Train Shuttle Activity
                Intent i = new Intent(ShuttleSelectionMenu.this, TrainShuttle.class);
                startActivity(i);
            }
        });

        mAirportButton = (Button) findViewById(R.id.airport_shuttle_button);
        mAirportButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Start Airport Shuttle Activity
                Intent i = new Intent(ShuttleSelectionMenu.this, AirportShuttle.class);
                startActivity(i);
            }
        });


    }



}
