package com.example.wren.bardcollegeshuttle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
/**
 * Created by chancewren on 2/16/17.
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private static String ONE_TIME = "onetime";
    private static String BUS_TIME = "busTime";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("AlarmManagerBroadcastReceiver.onReceive()::", "Alarm Received");

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock1 = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "AlarmManagerBroadcastReceiver.onReceive()");

        wakeLock1.acquire();

        callAlertDialogBox(context, intent);

        wakeLock1.release();

    }

    /**
     * This function displays a Toast on the screen and also plays an alert
     * sound for Alarm
     *
     * @param context
     *            Context of the application
     * @param intent
     *            Intent of the application
     */
    private void callAlertDialogBox(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        StringBuilder msg = new StringBuilder();

        if (!extras.equals(null) && extras.getBoolean(ONE_TIME, Boolean.FALSE)
                && !extras.getString(BUS_TIME).equals(null)) {
            msg.append("Time to Leave your bus will leave at : ");
            msg.append(extras.getString(BUS_TIME));
        }

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri.equals(null)) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

    }

    /**
     * This is the function which is called on click of 'Set Alarm' Button. It
     * sets the Alarm for time which is equal to Bus time minus the minutes
     * before which Alarm should ring.
     *
     * @param context
     *            Application Context
     * @param time
     *            Bus Time selected by the user
     * @param beforeMinutes
     *            Minutes before the Bus timing when the Alarm should start
     *            Ringing
     * @param selectedDate
     *            Date selected for setting Alarm
     */
    public String setOneTimeAlarm(Context context, String time, int beforeMinutes, String selectedDate) {

        Log.i("AlarmManagerBroadcastReceiver.setOneTimeAlarm()::", "Setting the Alarm");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;

        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            Log.e("AlarmManagerBroadcastReceiver.setOneTimeAlarm()::",
                    "Error occured while Parsing the Bus time, " + e.getMessage());
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        intent.putExtra(BUS_TIME, time);

        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date(selectedDate));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        calender.set(Calendar.HOUR, date.getHours());
        calender.set(Calendar.MINUTE, date.getMinutes() - beforeMinutes);

        Log.d("AlarmManagersetOneTimeAlarm()::", "Alarm set for : " + calender.getTime());

        alarmManager.set(AlarmManager.RTC, (calender.getTimeInMillis()), pendingIntent);

        return calender.getTime().toString();

    }

}
