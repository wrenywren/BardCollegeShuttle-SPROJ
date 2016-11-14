package com.example.wren.bardcollegeshuttle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class AreaShuttle extends AppCompatActivity {

    String areaDest;
    String areaDay;
    String areaDestandDay;
    Boolean wasAreaDayChosen = false;
    Boolean wasAreaDestChosen = false;


    String databaseTableName = "Area_Shuttle_Stops_Table";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Area Shuttle");
        setContentView(R.layout.activity_area_shuttle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Back Arrow Button
        getCurrentDate();
        populateShuttleDest();
        populateShuttleDays();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, ShuttleSelectionMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public String populateShuttleDest(){
        final DbBackend dbBackend = new DbBackend(AreaShuttle.this);
        final String[] areaShuttleStopsLists = dbBackend.getShuttleStops(databaseTableName); //populates list of stops
        final Button dayButton = (Button) findViewById(R.id.area_dest_button);
        dayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AreaShuttle.this);
                builder.setTitle("Choose a Destination: ");
                builder.setItems(areaShuttleStopsLists, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                areaDest = areaShuttleStopsLists[which].toString();
                                ((TextView)findViewById(R.id.area_dest_button)).setText(areaDest);
                                wasAreaDestChosen = true;
                                dayAndDestChosen();

                            }
                        }
                );
                builder.show();
            }
        });
        return areaDest;
    }

    public String populateShuttleDays(){
        final DbBackend dbBackend = new DbBackend(AreaShuttle.this);
        final String [] areaShuttleDaysList = dbBackend.getDaysforAreaShuttle(databaseTableName);
        final Button dayButton = (Button) findViewById(R.id.area_day_button);
        dayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AreaShuttle.this);
                builder.setTitle("Choose Day of Week: ");
                builder.setItems(areaShuttleDaysList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                areaDay = areaShuttleDaysList[which].toString();
                                ((TextView)findViewById(R.id.area_day_button)).setText(areaDay);
                                wasAreaDayChosen = true;
                                dayAndDestChosen();

                            }
                        }
                );
                builder.show();
            }
        });
        return areaDay;

    }

    public void dayAndDestChosen(){
        if (wasAreaDestChosen && wasAreaDayChosen){
            areaDestandDay = areaDest.concat(areaDay);
            populateAreaShuttleTimes(); //Populate Future Area Shuttle Times
        }
    }

    public void populateAreaShuttleTimes(){
        final DbBackend dbBackend = new DbBackend(AreaShuttle.this);
        final String [] areaShuttleTimesList= dbBackend.getDatesforAreaShuttle(areaDestandDay);
        final ListView lv = (ListView) findViewById(R.id.area_date_listView);
        final ArrayAdapter<String> areaTimeAdapter = new ArrayAdapter<String>(AreaShuttle.this, android.R.layout.simple_list_item_checked, areaShuttleTimesList);
        lv.setAdapter(areaTimeAdapter);
        Toast.makeText(this, areaDestandDay, Toast.LENGTH_SHORT).show();


    }

    public void getCurrentDate(){
        final DbBackend dbBackend = new DbBackend(AreaShuttle.this);
        String currentDate = dbBackend.getSQLDate();
        TextView textView = (TextView)findViewById(R.id.date_textView);
        textView.setText(currentDate);

    }





}
