package com.application.airnotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.application.airnotes.Data.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class register_screen extends AppCompatActivity {

    Button register_btn;
    EditText name, email_id, password;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        register_btn = findViewById(R.id.register_btn);
        name = findViewById(R.id.editTextName);
        email_id = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);
    }

    public void register_now(View view) {

        String username = name.getText().toString();
        String userMailId = email_id.getText().toString();
        String userPassword = password.getText().toString();

        if (!username.equals("")) {
            if (!userMailId.equals("")) {
                if (!userPassword.equals("")) {
                    register(username, userMailId, userPassword);
                } else {
                    Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Enter Your Email Address", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        }
    }

    private void register(final String username, final String userMailId, final String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userMailId, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onPreExecute();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    final String uid = currentUser.getUid();

                    //using data class storing user to database
                    UserInfo userInfo = new UserInfo(username,userMailId,"");

                    databaseReference.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                onPostExecute();
                                Toast.makeText(register_screen.this, "User is Registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(register_screen.this, "Failed to Create User", Toast.LENGTH_SHORT).show();
                                firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.e("Unsuccessful User", "Removed ",task.getException().getCause() );
                                        Intent intent = new Intent(register_screen.this,welcome_screen.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    });

                } else {
                    Log.e("Unsuccessful User", "Removed" ,task.getException().getCause()  );
                    Toast.makeText(register_screen.this, "Invalid Email Address or Password"  , Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    //through textView{Already a user}
    public void to_login_screen(View view) {
        Intent intent = new Intent(this, login_screeen.class);
        startActivity(intent);
    }

    //through imageView{backArrow}
    public void to_welcome_screen(View view) {
        Intent intent = new Intent(this, welcome_screen.class);
        startActivity(intent);
    }
    //Am using it in an AsyncTask. So in  my onPreExecute, I do this:
    public void onPreExecute() {
        progressDialog.show();
    }

    //dismiss in onPostExecute
    public void onPostExecute(){
        progressDialog.dismiss();
    }
}