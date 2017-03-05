package com.example.wren.bardcollegeshuttle;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.R.attr.delay;
import static android.content.ContentValues.TAG;

/**
 * Created by chancewren on 2/16/17.
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private static String ONE_TIME = "onetime";
    private static String BUS_TIME = "busTime";

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    private static Boolean notificationClick = false;

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("AlarmManagerBroadcastReceiver.onReceive()::", "Alarm Received");

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock1 = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "AlarmManagerBroadcastReceiver.onReceive()");

        wakeLock1.acquire();

        notificationAlert(context, intent);

        wakeLock1.release();


    }


    /**
     * This function displays a notification in the task bar and also plays an alert
     * sound and vibration for alarm.
     *
     * @param context
     *            Context of the application
     * @param intent
     *            Intent of the application
     */


    public void notificationAlert(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        if(intent != null)
        {
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    //example for large icon
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setContentTitle("Bard College Shuttle Alert")
                    .setContentText("Time to go your bus will leave at: " + extras.getString(BUS_TIME))
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            Intent i = new Intent(context, AlarmManagerBroadcastReceiver.class);
            pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);

            // example for blinking LED
            mBuilder.setLights(Color.RED, 200, 500);
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI); //default notification sound
            mBuilder.setContentIntent(pendingIntent);
            mNotifyMgr.notify(12345, mBuilder.build());
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // Start without a delay
            // Vibrate for 200 milliseconds
            // Sleep for 200 milliseconds
            //repeat 3 times
            long[] pattern = {0, 200, 200, 200, 200, 200, 200};
            // The '0' here means to repeat indefinitely
            // '0' is actually the index at which the pattern keeps repeating from (the start)
            // To repeat the pattern from any other point, you could increase the index, e.g. '1'
            vibrator.vibrate(pattern,-1);

        }


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
                    "Error occurred while Parsing the Bus time, " + e.getMessage());
        }

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        intent.putExtra(BUS_TIME, time);

        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date(selectedDate));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        calender.set(Calendar.HOUR, date.getHours());
        calender.set(Calendar.MINUTE, date.getMinutes() - beforeMinutes);

        Log.i("AlarmManagersetOneTimeAlarm()::", "Alarm set for : " + calender.getTime());

        alarmManager.set(AlarmManager.RTC_WAKEUP, (calender.getTimeInMillis()), pendingIntent);

        return calender.getTime().toString();

    }



}
