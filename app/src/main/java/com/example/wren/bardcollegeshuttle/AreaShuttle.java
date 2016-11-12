package com.example.wren.bardcollegeshuttle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class AreaShuttle extends AppCompatActivity {

    String day;
    Boolean dayChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Area Shuttle");
        setContentView(R.layout.activity_area_shuttle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Back Arrow Button
       getCurrentDate();
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

    String [] shuttleDays = {"Wednesday", "Friday", "Saturday"};


    public String populateShuttleDays(){
        //final DbBackend dbBackend = new DbBackend(AreaShuttle.this);
        //final String[] stopLists = dbBackend.getAllStops(); //populates list of stops
        final Button dayButton = (Button) findViewById(R.id.area_day_button);
        dayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AreaShuttle.this);
                builder.setTitle("Choose a Day: ");
                builder.setItems(shuttleDays, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on stopLists[which]
                                day = shuttleDays[which].toString();
                                ((TextView)findViewById(R.id.start_button)).setText(day);
                                dayChosen = true;
                                //twoButtonClicks();
                            }
                        }
                );
                builder.show();
            }
        });
        return day;
    }


    public void getCurrentDate(){
        final DbBackend dbBackend = new DbBackend(AreaShuttle.this);
        String currentDate = dbBackend.getFutureAreaShuttleDates();
        TextView textView = (TextView)findViewById(R.id.date_textView);
        textView.setText(currentDate);

    }




}
