package com.application.airnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.airnotes.Adapter.main_screen_adapter;
import com.application.airnotes.Data.UserNotes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class main_screen extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView start_textView;
    ImageView floating_button;
    private SpotsDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String received_title, received_desc;
    FirebaseUser current_user;
    DatabaseReference databaseReference, database_note;
    ArrayList<UserNotes> all_notes = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

//        searchView = findViewById(R.id.searchView);
        start_textView = findViewById(R.id.start_creating_textView);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar_addNotes);
        floating_button = findViewById(R.id.fab);
        toolbar.inflateMenu(R.menu.main_menu);
        setSupportActionBar(toolbar);
//        listView = (ListView) findViewById(R.id.lv1);

        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);
        sharedPreferences = getSharedPreferences("AIRNOTES_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Intent intent = getIntent();
        received_title = intent.getStringExtra("TITLE");
        received_desc = intent.getStringExtra("DESCRIPTION");

        current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        database_note = databaseReference.child(uid).child("user_notes");

        LinearLayoutManager linearLManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLManager);



        read_from_database();
    }

    //Reading from the Database
    public void read_from_database() {
        all_notes.clear();
        onPreExecute();
        database_note.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    UserNotes userNotes = snapshot.getValue(UserNotes.class);
                    all_notes.add(userNotes);
                }
                if (dataSnapshot.getValue() != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    start_textView.setVisibility(View.GONE);
                }

                main_screen_adapter adapter = new main_screen_adapter(main_screen.this, all_notes);
                recyclerView.setAdapter(adapter);
                onPostExecute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onPostExecute();
            }
        });
    }

    //get orientation configuration to change Layout Manager
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            read_from_database();

            LinearLayoutManager linearLManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLManager);
            //add your code what you want to do when screen on PORTRAIT MODE
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            read_from_database();
            GridLayoutManager gridLManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLManager);
            //add your code what you want to do when screen on LANDSCAPE MODE
        }
    }

    //Float button
    public void to_add_notes(View view) {
        Intent intent = new Intent(this, addNote_screen.class);
        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
        startActivity(intent);
    }

    //Am using it in an AsyncTask. So in  my onPreExecute, I do this:
    public void onPreExecute() {
        progressDialog.show();
    }

    //dismiss in onPostExecute
    public void onPostExecute() {
        progressDialog.dismiss();
    }

    //optionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.log_out:
                editor.putBoolean("LOGIN_STATUS",false);
                editor.commit();
                Intent intent = new Intent(this,welcome_screen.class);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                startActivity(intent);
                finish();
                return true;
            case R.id.help:
                Intent intent1 = new Intent(this,help_screen.class);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                startActivity(intent1);
                return true;
            case R.id.about_app:
                Intent intent2 = new Intent(this,abt_app_screen.class);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}