package com.example.wren.bardcollegeshuttle;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TrainShuttle extends Fragment {

    View myView;
    private String currentTime;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        myView = inflater.inflate(R.layout.activity_train_shuttle, container, false);
        return myView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        //getCurrentDate();
        //populateShuttleDest();
        //populateShuttleDays();

    }

    public void getCurrentDate(){
        final DbBackend dbBackend = new DbBackend(getActivity());
        String currentDate = dbBackend.getSQLDate();
        TextView textView = (TextView) getActivity().findViewById(R.id.date_textView);
        textView.setText("Today's Date: " + currentDate);
    }

    public void getCurrentTime(){
        final DbBackend dbBackend = new DbBackend(getActivity());
        currentTime = dbBackend.getCurrentTime();
        //((TextView)findViewById(R.id.Time_textView)).setText(currentTime);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(getActivity(), ShuttleSelectionDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
