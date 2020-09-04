package com.application.airnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.application.airnotes.databinding.ActivityWelcomeScreenBinding;

public class welcome_screen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    }

    public void to_login_screen(View view) {
        Intent intent = new Intent(this,login_screeen.class);
        startActivity(intent);
    }

    public void to_register_screen(View view) {
        Intent intent = new Intent(this,register_screen.class);
        startActivity(intent);
    }
}