package com.example.wren.bardcollegeshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Wren on 10/14/2016.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread timer = new Thread()
        {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally
                {
                    Intent i = new Intent(SplashActivity.this,ShuttleSelectionMenu.class);
                    finish();
                    startActivity(i);
                }
            }
        };
        timer.start();

    }



}
