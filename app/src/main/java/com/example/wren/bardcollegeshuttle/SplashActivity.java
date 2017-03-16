package com.example.wren.bardcollegeshuttle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * Created by Wren on 10/14/2016.
 */

public class SplashActivity extends Activity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screeen);
        StartAnimations();
    }
    Random mRandom = new Random();
    int low = 0;
    int high = 1;
    int result = mRandom.nextInt(high - low) + low;

    private void StartAnimations() {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
            anim.reset();
            LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
            l.clearAnimation();
            l.startAnimation(anim);

            anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
            anim.reset();
            ImageView iv = (ImageView) findViewById(R.id.bard_splash);
            iv.clearAnimation();
            iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashActivity.this,
                            ShuttleSelectionDrawer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }


//ON Screen touch skip to shuttle menu
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            splashTread.interrupt();
            Intent intent = new Intent(this,ShuttleSelectionDrawer.class);
            startActivity(intent);
        }
        return super.onTouchEvent(event);
    }

}

