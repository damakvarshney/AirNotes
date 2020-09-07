package com.application.airnotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class main_screen extends AppCompatActivity {

    Toolbar toolbar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        toolbar = findViewById(R.id.toolbar_addNotes);
        toolbar.setTitle("AirNotes");
        toolbar.setTitleTextColor(R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_notes_24);
    }

    public void to_add_notes(View view) {
        Intent intent = new Intent(this,addNote_screen.class);
        startActivity(intent);
    }
}