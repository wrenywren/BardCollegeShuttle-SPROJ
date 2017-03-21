package com.example.wren.bardcollegeshuttle;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CampusShuttle extends Fragment implements View.OnClickListener {

    View myView;
    Button shuttleTimes;

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


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        myView = inflater.inflate(R.layout.activity_campus_shuttle, container, false);
        shuttleTimes = (Button) myView.findViewById(R.id.allShuttleTimesButton);
        shuttleTimes.setOnClickListener(this);
        return myView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        populateStartStops();
        populateDestStops();

    }


    // Adds GPS Button
    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.shuttle_gps, menu);
        return getActivity().onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shuttle_map:
                //Toast.makeText(this, "Add Google Maps Class", Toast.LENGTH_SHORT).show();
                Intent Intent = new Intent(getActivity(), ShuttleMapsActivity.class); //Class for Maps
                startActivity(Intent);
                return true;
            // Adds Back Arrow Button
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(getActivity(), ShuttleSelectionDrawer.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public String populateStartStops(){
        final DbBackend dbBackend = new DbBackend(getActivity());
        final String[] stopLists = dbBackend.getShuttleStops(databaseTableName); //populates list of stops
        Button startButton = (Button) getActivity().findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose a Starting Point:");
                builder.setItems(stopLists, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                startStop = stopLists[which].toString();
                                TextView startButtonTextView = (TextView) getActivity().findViewById(R.id.start_button);
                                startButtonTextView.setText(startStop);
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
        final DbBackend dbBackend = new DbBackend(getActivity());
        final String[] stopLists = dbBackend.getShuttleStops(databaseTableName); //populates list of stops
        Button destButton = (Button) getActivity().findViewById(R.id.dest_button);
        destButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose a Destination:");
                builder.setItems(stopLists, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                destStop = stopLists[which].toString();
                                TextView destButtonTextView = (TextView) getActivity().findViewById(R.id.dest_button);
                                destButtonTextView.setText(destStop);
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
        final DbBackend dbBackend = new DbBackend(getActivity());
        Integer dayOfWeek = dbBackend.dayOfWeek();
        //((TextView)findViewById(R.id.DayofWeek_textView)).setText(Integer.toString(dayOfWeek));

    }



    public void getCurrentTime(){
        final DbBackend dbBackend = new DbBackend(getActivity());
        String currentTime = dbBackend.getCurrentTime();
        //((TextView)findViewById(R.id.Time_textView)).setText(currentTime);

    }


    public void populateFutureTimes(){
        ListView popFutureTimesListView = (ListView) getActivity().findViewById(R.id.times_listView);
        final DbBackend dbBackend = new DbBackend(getActivity());
        final String [] newTimes = dbBackend.getFutureTimesForStartAndDest(startdestStop); //populates ListView

        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, newTimes);
        popFutureTimesListView.setAdapter(timeAdapter);
        popFutureTimesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), newTimes[position] + " Shuttle Selected", Toast.LENGTH_SHORT).show();
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
        final NumberPicker numberPicker = new NumberPicker(getActivity());
        numberPicker.setMaxValue(60);
        numberPicker.setMinValue(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(numberPicker);
        builder.setTitle("Set Reminder for Shuttle");
        builder.setPositiveButton("SET REMINDER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setMinuteForAlarm = numberPicker.getValue();
                time = busTime;
                Log.i("setAlarmDialogBox::", "Time:" + time);
                Log.i("setAlarmDialogBox::", "BusTime:" + busTime);
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
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
        final DbBackend dbBackend = new DbBackend(getActivity());
        selectedDate = dbBackend.getSQLDate();

        alarm = new AlarmManagerBroadcastReceiver();

        Context context = getActivity().getApplicationContext();
        if (!alarm.equals(null)) {

            busAlarmTime = alarm.setOneTimeAlarm(context, time, setMinuteForAlarm, selectedDate);
            alarmSetAlertDialogBox(busAlarmTime);

        } else {
            Toast.makeText(context, "Alarm needs parameter", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View view) {
        if (startStop.equals("") || destStop.equals("")){
            Toast.makeText(getActivity(), "Please select starting point and destination", Toast.LENGTH_SHORT).show();
        }else {
            Intent myIntent = new Intent(getActivity(), AllCampusShuttleTimes.class);
            myIntent.putExtra("extra", startdestStop);
            startActivity(myIntent);
        }
    }
}

