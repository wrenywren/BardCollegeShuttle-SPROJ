package com.example.wren.bardcollegeshuttle;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CampusShuttle extends AppCompatActivity{

    String startStop = ""; //Selected Starting Point is stored here
    String destStop = "";    //Selected Destination is stored here
    String startdestStop = ""; //Join start and destination Strings
    boolean startButtonClicked = false;
    boolean destButtonClicked = false;

    String databaseTableName = "Stops_Table";

    private String time = "";
    private String selectedDate = "";
    private String busAlarmTime = "";
    private AlarmManagerBroadcastReceiver alarm;

    private int setMinuteForAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Campus Shuttle"); // Title of Page (Activity) on Toolbar
        setContentView(R.layout.activity_campus_shuttle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Back Arrow Button
        populateStartStops();
        populateDestStops();


    }

    // Adds GPS Button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shuttle_gps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shuttle_map:
                //Toast.makeText(this, "Add Google Maps Class", Toast.LENGTH_SHORT).show();
                Intent Intent = new Intent(this, ShuttleMapsActivity.class); //Class for Maps
                startActivity(Intent);
                return true;
            // Adds Back Arrow Button
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, ShuttleSelectionMenu.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public String populateStartStops(){
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        final String[] stopLists = dbBackend.getShuttleStops(databaseTableName); //populates list of stops
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
        final String[] stopLists = dbBackend.getShuttleStops(databaseTableName); //populates list of stops
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
        //((TextView)findViewById(R.id.DayofWeek_textView)).setText(Integer.toString(dayOfWeek));

    }



    public void getCurrentTime(){
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        String currentTime = dbBackend.getCurrentTime();
        //((TextView)findViewById(R.id.Time_textView)).setText(currentTime);

    }


    public void populateFutureTimes(){
        final ListView lv = (ListView) findViewById(R.id.times_listView);
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        final String [] newTimes = dbBackend.getFutureTimesForStartAndDest(startdestStop); //populates ListView

        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(CampusShuttle.this, android.R.layout.simple_list_item_checked, newTimes);
        lv.setAdapter(timeAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CampusShuttle.this, newTimes[position] + " Shuttle Selected", Toast.LENGTH_SHORT).show();
                setAlarmDialogBox(newTimes[position]);

            }
        });

    }

    private void twoButtonClicks(){
        if (startButtonClicked && destButtonClicked){
            startdestStop = startStop.concat(destStop);
            //checkDayofWeek(); // prints day of week WILL REMOVE AFTER TESTING
            populateFutureTimes(); //Populate Future Times
        }
    }



    //function to set alarm minutes
    public void setAlarmDialogBox(final String busTime){
        final NumberPicker numberPicker = new NumberPicker(CampusShuttle.this);
        numberPicker.setMaxValue(60);
        numberPicker.setMinValue(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(CampusShuttle.this);
        builder.setView(numberPicker);
        builder.setTitle("Set Reminder for Shuttle");
        builder.setPositiveButton("SET REMINDER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setMinuteForAlarm = numberPicker.getValue();
                time = busTime;
                onetimeTimer();
            }
        });
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * This function displays an Alert Dialog Box with 'Ok' button, informing
     * user that alarm has been set for the selected date and time
     *
     * @param busTime
     *            This parameter holds the value of time for which alarm is set
     *
     */
    private void alarmSetAlertDialogBox(String busTime) {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(CampusShuttle.this);

        alertDialogBuilder.setTitle("Bard College Shuttle Alert ");
        alertDialogBuilder.setMessage("Your alarm is set for: " + busTime).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    /**
     * This function is used to set one time Alarm based on the time and date
     * selected by the user
     */
    public void onetimeTimer() {
        final DbBackend dbBackend = new DbBackend(CampusShuttle.this);
        selectedDate = dbBackend.getSQLDate();

        alarm = new AlarmManagerBroadcastReceiver();

        Context context = this.getApplicationContext();
        if (!alarm.equals(null)) {

            busAlarmTime = alarm.setOneTimeAlarm(context, time, setMinuteForAlarm, selectedDate);
            alarmSetAlertDialogBox(busAlarmTime);

        } else {
            Toast.makeText(context, "Alarm needs parameter", Toast.LENGTH_LONG).show();
        }
    }





}

