package com.application.airnotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.application.airnotes.Data.UserNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rockerhieu.emojicon.EmojiconEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.FontRequest;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class main_screen extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<UserNotes> all_notes = new ArrayList<>();
    RecyclerView recyclerView;
    TextView start_textView,light_mode,dark_mode;
    ImageView floating_button;
    DatabaseReference databaseReference,database_note;
    FirebaseUser current_user;
    private SpotsDialog progressDialog;
    String received_title,received_desc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        start_textView = findViewById(R.id.start_creating_textView);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar_addNotes);
        floating_button = findViewById(R.id.fab);

        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);



        Intent intent = getIntent();
        received_title=intent.getStringExtra("TITLE");
        received_desc = intent.getStringExtra("DESCRIPTION");

        current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        database_note= databaseReference.child(uid).child("user_notes");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        onPreExecute();
        read_from_database();
        Log.e("check_now", database_note.getKey());






    }

    public void to_add_notes(View view) {
        Intent intent = new Intent(this,addNote_screen.class);
        startActivity(intent);
    }

    public void read_from_database(){
        all_notes.clear();
        onPreExecute();
        database_note.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("DataSnapShot", String.valueOf(dataSnapshot));
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.e("tag",snapshot.toString() );
                    UserNotes userNotes = snapshot.getValue(UserNotes.class);
                    all_notes.add(userNotes);
                }
                if(dataSnapshot.getValue()!=null){
                    recyclerView.setVisibility(View.VISIBLE);
                    start_textView.setVisibility(View.GONE);
                }
                onPostExecute();
                main_screen_adapter adapter = new main_screen_adapter(main_screen.this,all_notes);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onPostExecute();
            }
        });
    }


    //Am using it in an AsyncTask. So in  my onPreExecute, I do this:
    public void onPreExecute() {
        progressDialog.show();
    }

    //dismiss in onPostExecute
    public void onPostExecute() {
        progressDialog.dismiss();
    }


}