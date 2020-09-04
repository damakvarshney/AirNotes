package com.application.airnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class splash_screen extends AppCompatActivity {

    private ProgressBar pgsBar;
    private int i = 0;
    private Handler hdlr = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        pgsBar = findViewById(R.id.pBar);

        i = pgsBar.getProgress();

        new Thread(new Runnable() {
            public void run() {
                while (i < 100) {
                    i += 5;
                    // Update the progress bar and display the current value in text view
                    hdlr.post(new Runnable() {
                        public void run() {
                            pgsBar.setProgress(i);
                        }
                    });
                    try {
                        // Sleep for 100 milliseconds to show the progress slowly.
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Do stuff when progress is max
                if (i == 100)
                    startActivity(new Intent(splash_screen.this, welcome_screen.class));
                finish();
            }
        }).start();
    }
}