package com.example.wren.bardcollegeshuttle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wren on 10/23/2016.
 */

public class DbBackend extends DbObject {
    public DbBackend(Context context) {
        super(context);
    }



    // Function to populate dialog window with Bard College Shuttle Stops for selected database
    public String[] getShuttleStops(String databaseName){
        String query = "Select * from '"+databaseName+"'";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                String word = cursor.getString(cursor.getColumnIndexOrThrow("stop_name"));
                spinnerContent.add(word);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        String[] allStops = new String[spinnerContent.size()];
        allStops = spinnerContent.toArray(allStops);
        return allStops;
    }


        //Function to populate day option for area shuttle
    public String[] getDaysforAreaShuttle(String databaseName){
        int count = 0;
        String query = "SELECT * FROM '"+databaseName+"'";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        ArrayList<String> daysofWeekList = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do {
                String day = cursor.getString(cursor.getColumnIndexOrThrow("day_id"));
                daysofWeekList.add(day);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        String[] allListView = new String[daysofWeekList.size()];
        allListView = daysofWeekList.toArray(allListView);
        return allListView;
    }

        //Function to populate day option for area shuttle
    public String[] getDatesforAreaShuttle(String areaDestandDate){

        // Get current date. MM/DD/YYYY
        String currentDate = "";
        int cYear = 0;
        int cMonth = 0;
        int cDay = 0;
        String selectCurrentDateQ = "SELECT date('now', 'localtime')";
        Cursor sqlDateCursor         = this.getDbConnection().rawQuery(selectCurrentDateQ,null);
        if (sqlDateCursor.moveToFirst()) {
            String [] currentDateSplit = sqlDateCursor.getString(0).split("-");
            String currentYear = currentDateSplit[0];
            String currentMonth = currentDateSplit[1];
            String currentDay = currentDateSplit[2];
            currentDate = currentMonth +"-"+ currentDay +"-"+ currentYear;

            cYear = Integer.parseInt(currentYear); // currentYear in integer form
            cMonth = Integer.parseInt(currentMonth); //currentMonth in Integer form
            cDay = Integer.parseInt(currentDay); //currentDay in Integer form

        }

        String databseDateQuery = "";
        if (cMonth == 8 || cMonth == 9 || cMonth == 10 || cMonth == 11 || cMonth == 12)
        {
            //ACCESS FALL AREA SHUTTLE SCHEDULE
            databseDateQuery = "SELECT * FROM Area_Shuttle_Fall_Date_Table as ASFDT WHERE ASFDT.date_id LIKE '" + areaDestandDate + "' ";
        }else if (cMonth == 1 || cMonth == 2 || cMonth == 3 || cMonth == 4 || cMonth == 5)
        {
            //ACCESS SPRING AREA SHUTTLE SCHEDULE
            databseDateQuery = "SELECT * FROM Area_Shuttle_Spring_Date_Table as ASSDT WHERE ASSDT.date_id LIKE '" + areaDestandDate + "' ";
        }else{
            //ADD SOME OTHER DATABASE FOR ANOTHER RANGE OF MONTHS IN THE YEAR
        }

        Cursor cursor = this.getDbConnection().rawQuery(databseDateQuery, null);
        ArrayList<String> daysofWeekList = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do {
                String day = cursor.getString(cursor.getColumnIndexOrThrow("shuttle_date"));
                String [] databaseDateSplit = day.split("/");
                String databaseMonth = databaseDateSplit[0];
                String databaseDay = databaseDateSplit[1];
                //String databaseYear = databaseDateSplit[2];
                String databaseDate = databaseMonth +"/"+ databaseDay +"/"+ cYear;

                //int dYear = Integer.parseInt(databaseYear); // databaseYear in integer form
                int dMonth = Integer.parseInt(databaseMonth); //databaseMonth in Integer form
                int dDay = Integer.parseInt(databaseDay); //databaseDay in Integer form

                if (dMonth >= cMonth && dDay >= cDay){
                    daysofWeekList.add(day + "/" + cYear);
                }else if (dMonth > cMonth) {
                    daysofWeekList.add(day + "/" + cYear);
                }else{
                    continue;
                }

            }
            while(cursor.moveToNext());
        }
        cursor.close();
        String[] allListView = new String[daysofWeekList.size()];
        allListView = daysofWeekList.toArray(allListView);
        return allListView;
    }



    //Function to get currentDate for testing
    public String getSQLDate(){

        // Get current date. MM/DD/YYYY
        String currentDate = "";
        String selectCurrentDateQ = "SELECT date('now', 'localtime')";
        Cursor sqlDateCursor         = this.getDbConnection().rawQuery(selectCurrentDateQ,null);
        if (sqlDateCursor.moveToFirst()) {
            String [] currentDateSplit = sqlDateCursor.getString(0).split("-");
            String currentYear = currentDateSplit[0];
            String currentMonth = currentDateSplit[1];
            String currentDay = currentDateSplit[2];
            currentDate = currentMonth +"-"+ currentDay +"-"+ currentYear;

            int cYear = Integer.parseInt(currentYear); // currentYear in integer form
            int cMonth = Integer.parseInt(currentMonth); //currentMonth in Integer form
            int cDay = Integer.parseInt(currentDay); //currentDay in Integer form

        }
        sqlDateCursor.close();
        return currentDate;
    }



    

    //Function to List Future Area Shuttle dates
    public String getFutureAreaShuttleDates(){

        // Get current date. MM/DD/YYYY
        String currentDate = "";
        String selectCurrentDateQ = "SELECT date('now', 'localtime')";
        Cursor sqlDateCursor         = this.getDbConnection().rawQuery(selectCurrentDateQ,null);
        if (sqlDateCursor.moveToFirst()) {
            String [] currentDateSplit = sqlDateCursor.getString(0).split("-");
            String currentYear = currentDateSplit[0];
            String currentMonth = currentDateSplit[1];
            String currentDay = currentDateSplit[2];
            currentDate = currentMonth +"-"+ currentDay +"-"+ currentYear;

            int cYear = Integer.parseInt(currentYear); // currentYear in integer form
            int cMonth = Integer.parseInt(currentMonth); //currentMonth in Integer form
            int cDay = Integer.parseInt(currentDay); //currentDay in Integer form

        }
        ArrayList<String> sqlDateArray = new ArrayList<>();
        String databaseDate = "";
        String selectDatabaseDateQ = "";
        Cursor databaseDateCursor         = this.getDbConnection().rawQuery(selectCurrentDateQ,null);
        if (databaseDateCursor.moveToFirst()){
            String [] databaseDateSplit = databaseDateCursor.getString(0).split("-");
            String databaseYear = databaseDateSplit[0];
            String databaseMonth = databaseDateSplit[1];
            String databaseDay = databaseDateSplit[2];
            databaseDate = databaseMonth +"-"+ databaseDay +"-"+ databaseYear;
        }


        sqlDateCursor.close();
        return currentDate;
    }



    //Function to List Future Times for Start and Destination
    public String [] getFutureTimesForStartAndDest(String startdestStop){
        ArrayList<String> timeArray = new ArrayList<String>();

        //Get current hour and min
        Integer currentHour = 0;
        Integer currentMinute = 0;
        String currentHr = "";
        String currentMin= "";
        String currentTime= "";
        int cHour = 0;
        int cMinute = 0;
        String selectCurrentTimeQ = "SELECT STRFTIME('%H','NOW','LOCALTIME') AS HOUR, STRFTIME('%M','NOW','LOCALTIME') AS MINUTE ";
        Cursor cur          = this.getDbConnection().rawQuery(selectCurrentTimeQ,null);
        String[] currentTimeSplit = new String[2];

        if(cur.moveToFirst()) {

            currentHour = Integer.parseInt(cur.getString(0));
            currentMinute = Integer.parseInt(cur.getString(1));
            currentTime = currentHour + ":" + currentMinute;
            currentTimeSplit = currentTime.split(":");
            currentHr = (currentTimeSplit[0]);
            currentMin = (currentTimeSplit[1]);
            cHour = Integer.parseInt(currentHr);
            cMinute = Integer.parseInt(currentMin);

        }

        String selectDatabaseTimeQ = "";
        
        if (dayOfWeek() == 5 || dayOfWeek() == 6){ //Thursday or Friday
            //if dayofweek is thursday or friday query thursday and friday night time schedule
            selectDatabaseTimeQ = "SELECT * FROM Time_Table_Thur_Fri as TT WHERE TT.stop_id LIKE '"+startdestStop+"' ";
        }else if (dayOfWeek() == 7){ //Saturday
            //if day of week is saturday query saturday time schedule
            selectDatabaseTimeQ = "SELECT * FROM Time_Table_Saturday as TT WHERE TT.stop_id LIKE '"+startdestStop+"' ";
        }else if(dayOfWeek() == 1){ //Sunday
            //if day of week is sunday query sunday time schedule
            selectDatabaseTimeQ = "SELECT * FROM Time_Table_Sunday as TT WHERE TT.stop_id LIKE '"+startdestStop+"' ";
        }else{
            //else query regular monday through Wed. schedule
            selectDatabaseTimeQ = "SELECT * FROM Time_Table_Mon_Wed as TT WHERE TT.stop_id LIKE '"+startdestStop+"' ";
        }


        //Get time from database
        String databaseTime = "";
        Integer databaseHour = 0;
        Integer databaseMinute = 0;
        String databaseHr = "";
        String databaseMin= "";
        Cursor cursor1 = this.getDbConnection().rawQuery(selectDatabaseTimeQ,null);
        ArrayList<String> databaseTimes = new ArrayList<String>();

        if (cursor1.moveToFirst()){

            String[] databaseTimeSplit = new String[2];
            if(cursor1.moveToFirst()) {
                do {
                    String time = cursor1.getString(cursor1.getColumnIndexOrThrow("shuttle_time"));
                        //add AM to morning time && add PM to afternoon time
                        databaseTimeSplit = time.split(" ");
                        databaseHr = (databaseTimeSplit[0]);
                        databaseMin = (databaseTimeSplit[1]);
                        databaseHour = Integer.parseInt(databaseHr);
                        databaseMinute = Integer.parseInt(databaseMin);

                        if (databaseHour >= 12) {
                            databaseTime = databaseHour + ":" + databaseMinute + " PM";
                        } else{
                            databaseTime = databaseHour + ":" + databaseMinute + " AM";
                        }

                        //only add times that haven't passed yet
                        if (databaseHour >= cHour)
                        {
                            if(databaseHour > cHour) {
                                time = databaseTime;
                                databaseTimes.add(time);
                            }
                            if(databaseHour == cHour && databaseMinute > cMinute) {
                                time = databaseTime;
                                databaseTimes.add(time);
                            }

                        }
                        if(databaseHour == 0 || databaseHour == 1 || databaseHour == 2 ){
                          time = databaseTime;
                            databaseTimes.add(time);
                        }


                    }
                    while (cursor1.moveToNext()) ;
                }

           for (int i = 0; i < databaseTimes.size(); i++) {
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ");
                    final Date dateObj = sdf.parse(databaseTimes.get(i));
                    if (!timeArray.contains(new SimpleDateFormat("h:mm a").format(dateObj))){
                        timeArray.add(new SimpleDateFormat("h:mm a").format(dateObj));
                    }else{
                        continue;
                    }
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor1.close();
        String[] allListView = new String[timeArray.size()];
        allListView = timeArray.toArray(allListView);
        return allListView;

    }

    //Function to check current day of week. (i.e Monday, Tuesday)
    public Integer dayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay;
    }



    /**
     * getCurrentTime is used to get the current local time, which is used to filter out the future bus timings.
     *
     * @return
     */
    public String getCurrentTime(){

        SQLiteDatabase sqlDB = null;
        String currentTime = null;
        Integer currentHr = 0;
        Integer currentMin = 0;

        if(sqlDB == null || !sqlDB.isOpen() ) {
            sqlDB = this.getDbConnection();
        }

        try{

            String selectCurrentTimeQ = " SELECT STRFTIME('%H','NOW','LOCALTIME') AS HOUR, STRFTIME('%M','NOW','LOCALTIME') AS MINUTE ";
            Cursor cur          = sqlDB.rawQuery(selectCurrentTimeQ,null);
            if(cur.moveToFirst())

            {
                String currentHour     = (cur.getString(0));
                String currentMinute   = (cur.getString(1));
                currentHr              = Integer.parseInt(currentHour);
                currentMin             = Integer.parseInt(currentMinute);
                if(currentHr >= 12){
                    currentTime         = currentHour + ":" + currentMinute + " PM";
                }else{
                    currentTime         = currentHour + ":" + currentMinute + " AM";
                }
            }

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            sqlDB.close();
        }
        return currentTime;
    }




}
