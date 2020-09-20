package com.application.airnotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.application.airnotes.Data.UserNotes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class addNote_screen extends AppCompatActivity {

    Toolbar toolbar;
    EditText title,description;
    DatabaseReference databaseReference;
    FirebaseUser current_user;
    private SpotsDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String context_title;
    String context_desc;
    String context_id;
    String timeStamp;
    String uid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_screen);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AirNotes");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addNote_screen.this, main_screen.class);
                onBackPressed();
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                startActivity(intent);
            }
        });
        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Getting data for editing
        Intent intent = getIntent();
        context_title=intent.getStringExtra("TITLE");
        context_desc=intent.getStringExtra("DESCRIPTION");
        context_id = intent.getStringExtra("NOTE_ID");
        title.setText(context_title);
        description.setText(context_desc);


    }

    //Save onBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        editor.putString("title", title.getText().toString());
        editor.putString("description", description.getText().toString());
        editor.apply();
        editor.commit();
        super.onBackPressed();


        context_title = title.getText().toString();
        context_desc = description.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String context_date = simpleDateFormat.format(calendar.getTime());

        if(!context_title.equalsIgnoreCase("") || !context_desc.equalsIgnoreCase("")){
            if(context_id==null || context_id.isEmpty()){
                save_notes_to_database(context_title,context_desc,context_date);
            }else {
                edited_note_to_database(context_title,context_desc,context_date);
            }
        }else{
            return;
        }

    }

    //Save NOTES
    private void save_notes_to_database(String context_title, String context_desc, String context_date) {
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();
        Long tsLong = System.currentTimeMillis() / 1000;
        timeStamp = tsLong.toString();
        UserNotes userNotes = new UserNotes(timeStamp, context_date, context_title, context_desc);


        databaseReference.child(uid).child("user_notes").child(timeStamp).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(addNote_screen.this, "Your Note is Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addNote_screen.this, main_screen.class);
                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(addNote_screen.this, "Error in saving your Notes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Edit Notes
    private void edited_note_to_database(String context_title, String context_desc, String context_date) {
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();
        UserNotes userNotes = new UserNotes(context_id,context_date,context_title,context_desc);

        databaseReference.child(uid).child("user_notes").child(context_id).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(addNote_screen.this, "Your Note is Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addNote_screen.this,main_screen.class);
                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                    startActivity(intent);
                    finish();
                }
                else {

                    Toast.makeText(addNote_screen.this, "Error in saving your Notes", Toast.LENGTH_SHORT).show();
                }
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
