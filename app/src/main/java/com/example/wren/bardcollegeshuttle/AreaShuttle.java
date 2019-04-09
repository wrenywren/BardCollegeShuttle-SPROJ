package com.example.wren.bardcollegeshuttle;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AreaShuttle extends Fragment {

    View myView;
    String areaDest;
    String areaDay;
    String areaDestandDay;
    Boolean wasAreaDayChosen = false;
    Boolean wasAreaDestChosen = false;
    String databaseTableName = "Area_Shuttle_Stops_Table";
    String areaShuttleInfo;

    private String time = "";
    private String selectedDate = "";
    private String busAlarmTime = "";
    private AlarmManagerBroadcastReceiver alarm;
    private String departureDate = "";


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        myView = inflater.inflate(R.layout.activity_area_shuttle, container, false);
        return myView;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        getCurrentDate();
        populateShuttleDest();
        populateShuttleDays();

    }



    public String populateShuttleDest(){
        final DbBackend dbBackend = new DbBackend(getActivity());
        final String[] areaShuttleStopsLists = dbBackend.getShuttleStops(databaseTableName); //populates list of stops
        final Button dayButton = getActivity().findViewById(R.id.area_dest_button);
        dayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose a Destination: ");
                builder.setItems(areaShuttleStopsLists, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                areaDest = areaShuttleStopsLists[which].toString();
                                ((TextView) getActivity().findViewById(R.id.area_dest_button)).setText(areaDest);
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
        final DbBackend dbBackend = new DbBackend(getActivity());
        final String [] areaShuttleDaysList = dbBackend.getDaysforAreaShuttle(databaseTableName);
        final Button dayButton = getActivity().findViewById(R.id.area_day_button);
        dayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Day of Week: ");
                builder.setItems(areaShuttleDaysList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                areaDay = areaShuttleDaysList[which];
                                ((TextView) getActivity().findViewById(R.id.area_day_button)).setText(areaDay);
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
            if (areaDestandDay.equals("Hudson Valley Mall, KingstonWednesday")){
                areaShuttleInfo = getString(R.string.mall_wed);
            } else if (areaDestandDay.equals("Hudson Valley Mall, KingstonSaturday")){
                areaShuttleInfo = getString(R.string.mall_sat);
            } else if (areaDestandDay.equals("Downtown RhinebeckFriday")){
                areaShuttleInfo = getString(R.string.rhinebeck_fri);
            } else if (areaDestandDay.equals("Woodbury Commons Mall (Upscale Outlet Stores)Saturday")){
                areaShuttleInfo = getString(R.string.woodbury_sat);
            }
            populateAreaShuttleTimes(); //Populate Future Area Shuttle Times
        }
    }

    public void populateAreaShuttleTimes(){
        final DbBackend dbBackend = new DbBackend(getActivity());
        final String [] areaShuttleTimesList = dbBackend.getDatesforAreaShuttle(areaDestandDay);
        final ListView lv = getActivity().findViewById(R.id.area_date_listView);
        final ArrayAdapter<String> areaTimeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_checked, areaShuttleTimesList);
        lv.setAdapter(areaTimeAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), areaShuttleTimesList[position] + " Date Selected", Toast.LENGTH_SHORT).show();
                infoDialogBox(areaShuttleTimesList[position]);

            }
        });
    }


    //function to set alarm for Area Shuttle
    public void infoDialogBox(final String busDate){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Bard College Shuttle Alert");
        builder.setMessage(areaShuttleInfo);
        builder.setCancelable(false);
        builder.setPositiveButton("SET REMINDER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAlarmDialogBox(busDate);
                dialog.cancel();

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

    public void getCurrentDate(){
        final DbBackend dbBackend = new DbBackend(getActivity());
        String currentDate = dbBackend.getSQLDate();
        TextView textView = getActivity().findViewById(R.id.date_textView);
        textView.setText("Today's Date: " + currentDate);
    }


    public void setAlarmDialogBox(final String busDate){
        final TimePicker timePicker = new TimePicker((getActivity()));
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();
        final String dTime = hour + ":" + min;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(timePicker);
        builder.setTitle("Set Reminder for Shuttle");
        builder.setPositiveButton("SET REMINDER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                time = dTime;
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(time);
                    time = new SimpleDateFormat("K:mm a").format(dateObj);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
                departureDate = busDate;
                Log.i("setAlarmDialogBox::", "AlarmTime:" + time);
                Log.i("setAlarmDialogBox::", "BusDate:" + busDate);
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
        selectedDate = departureDate;

        alarm = new AlarmManagerBroadcastReceiver();

        Context context = getActivity().getApplicationContext();
        if (!alarm.equals(null)) {

            busAlarmTime = alarm.setAreaShuttleOneTimeAlarm(context, time, selectedDate);
            alarmSetAlertDialogBox(busAlarmTime);

        } else {
            Toast.makeText(context, "Alarm needs parameter", Toast.LENGTH_LONG).show();
        }
    }






}
