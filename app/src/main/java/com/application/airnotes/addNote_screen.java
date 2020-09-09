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
                startActivity(intent);
            }
        });
        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
//          this was for setText back when entered
//        String restored_title = prefs.getString("title", null);
//        if(!TextUtils.isEmpty(restored_title)){
//            title.setText(restored_title);
//        }
//        String restored_description = prefs.getString("description", null);
//        if(!TextUtils.isEmpty(restored_description)){
//            description.setText(restored_description);
//        }

    }

    public void click_for_save(View view) {
        String context_title = title.getText().toString();
        String context_desc = description.getText().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        Calendar calendar = Calendar.getInstance();
        String todays_date = simpleDateFormat.format(calendar.getTime());

        if(!context_title.equalsIgnoreCase("")){
            if(!context_desc.equalsIgnoreCase("")){
                onPreExecute();
                    save_notes_to_database(context_title,context_desc,todays_date);

            }else {
                Toast.makeText(this, "Enter the Description", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Enter the Title", Toast.LENGTH_SHORT).show();
        }
    }

    private void save_notes_to_database(String context_title, String context_desc, String todays_date) {
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();


        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = tsLong.toString();


        UserNotes userNotes = new UserNotes(timeStamp,todays_date,context_title,context_desc);

        databaseReference.child(uid).child("user_notes").child(timeStamp).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    onPostExecute();
                    Toast.makeText(addNote_screen.this, "Your Note is Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(addNote_screen.this,main_screen.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    onPostExecute();
                    Toast.makeText(addNote_screen.this, "Error in saving your Notes", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("title", title.getText().toString());
        editor.putString("description", description.getText().toString());
        editor.apply();
        editor.commit();
        super.onBackPressed();

        String context_title = title.getText().toString();
        String context_desc = description.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD MM YYYY");
        Calendar calendar = Calendar.getInstance();
        String todays_date = simpleDateFormat.format(calendar.getTime());

        if(!context_title.equalsIgnoreCase("") || !context_desc.equalsIgnoreCase("")){
            save_notes_to_database(context_title,context_desc,todays_date);
        }else{
            return;
        }

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