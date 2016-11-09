package com.example.wren.bardcollegeshuttle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;


public class CampusShuttle extends AppCompatActivity{

    String startStop = ""; //Selected Starting Point is stored here
    String destStop = "";    //Selected Destination is stored here
    String startdestStop = ""; //Join start and destination Strings
    boolean startButtonClicked = false;
    boolean destButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Campus Shuttle"); // Title of Page (Activity) on Toolbar
        setContentView(R.layout.activity_campus_shuttle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Back Arrow Button
        populateStartStops();
        populateDestStops();


    }


    public String populateStartStops(){
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        final String[] stopLists = dbBackend.getAllStops(); //populates list of stops
        final Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CampusShuttle.this);
                builder.setTitle("Choose a Starting Point:");
                builder.setItems(stopLists, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                startStop = stopLists[which].toString();
                                ((TextView)findViewById(R.id.start_button)).setText(startStop);
                                startButtonClicked = true;
                                twoButtonClicks();
                            }
                        }
                );
                builder.show();
            }
        });
        return startStop;
    }


    public String populateDestStops(){
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        final String[] stopLists = dbBackend.getAllStops(); //populates list of stops
        final Button destButton = (Button) findViewById(R.id.dest_button);
        destButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CampusShuttle.this);
                builder.setTitle("Choose a Destination:");
                builder.setItems(stopLists, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                destStop = stopLists[which].toString();
                                ((TextView)findViewById(R.id.dest_button)).setText(destStop);
                                destButtonClicked = true;
                                twoButtonClicks();
                            }
                        }
                );
                builder.show();
            }
        });
        return destStop;
    }




    public void checkDayofWeek(){
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        Integer dayOfWeek = dbBackend.dayOfWeek();
        ((TextView)findViewById(R.id.DayofWeek_textView)).setText(Integer.toString(dayOfWeek));

    }



    public void getCurrentTime(){
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        String currentTime = dbBackend.getCurrentTime();
        //((TextView)findViewById(R.id.Time_textView)).setText(currentTime);

    }



    public void populateTimes(){
        final ListView lv = (ListView) findViewById(R.id.times_listView);
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        String[] listViewOfTimes = dbBackend.getAllTimesForStart(startStop); //populates ListView
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(CampusShuttle.this, android.R.layout.simple_list_item_1, listViewOfTimes);
        lv.setAdapter(timeAdapter);

    }


    public void populateFutureTimes(){
        final ListView lv = (ListView) findViewById(R.id.times_listView);
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        final String [] newTimes = dbBackend.getFutureTimesForStartAndDest(startdestStop); //populates ListView
        //String [] listViewOfTimes = dbBackend.convertTime(newTimes); //converts listView of 24hour times to 12 hour times
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(CampusShuttle.this, android.R.layout.simple_list_item_1, newTimes);
        lv.setAdapter(timeAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CampusShuttle.this, newTimes[position] + " Shuttle Selected", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void twoButtonClicks(){
        if (startButtonClicked && destButtonClicked){
            startdestStop = startStop.concat(destStop);
            checkDayofWeek(); // prints day of week WILL REMOVE AFTER TESTING
            populateFutureTimes(); //Populate Future Times
        }
    }




    //Back Arrow Button
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


}



















