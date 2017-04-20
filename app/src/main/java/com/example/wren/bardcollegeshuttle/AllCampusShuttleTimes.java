package com.example.wren.bardcollegeshuttle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by wreny on 3/19/2017.
 */

public class AllCampusShuttleTimes extends Activity {
    public String mStartDestStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_campus_shuttle_times);
        mStartDestStop = getIntent().getStringExtra("extra");
        populateAllTimes();

    }


    public void onCreatedView(Bundle savedInstanceState){

    }

    public void populateAllTimes(){
        ListView popAllTimesListView = (ListView) this.findViewById(R.id.allShuttleTimes_listView);
        final DbBackend dbBackend = new DbBackend(this);
        final String [] newTimes = dbBackend.getAllTimesForStartAndDest(mStartDestStop); //populates ListView
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, newTimes);
        popAllTimesListView.setAdapter(timeAdapter);
        popAllTimesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AllCampusShuttleTimes.this, newTimes[position] + " Shuttle Selected", Toast.LENGTH_SHORT).show();
                //setAlarmDialogBox(newTimes[position]);

            }
        });

    }



}
