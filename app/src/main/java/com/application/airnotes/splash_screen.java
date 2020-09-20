package com.application.airnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash_screen extends AppCompatActivity {

    private Handler hdlr = new Handler();
    SharedPreferences sharedPreferences;
    private final int SPLASH_DISPLAY_LENGTH = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        sharedPreferences = getSharedPreferences("AIRNOTES_DATA", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (sharedPreferences.getBoolean("LOGIN_STATUS", false)) {
                    startActivity(new Intent(splash_screen.this, main_screen.class));
                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                    finish();
                } else {
                    startActivity(new Intent(splash_screen.this, welcome_screen.class));
                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}

