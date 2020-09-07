package com.application.airnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.airnotes.Data.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class link_with_existing_account extends AppCompatActivity {
    EditText email, password,name;
    AuthCredential credential;
    private SpotsDialog progressDialog;
    String userPhoneNumber, userEmail, userPassword,username;
    Button link_btn;
    public static DatabaseReference databaseReference;
    public static FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_with_existing_account);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        name = findViewById(R.id.editTextName);
        link_btn = findViewById(R.id.link_btn);

        Intent intent = getIntent();
        userPhoneNumber = intent.getStringExtra("MOBILE_NUMBER");

        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

    }

    //signIn with Email
    private void updateUI() {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser currentUser = task.getResult().getUser();
                        String uid = currentUser.getUid();

                        //using data class storing user to database
                        if (task.isSuccessful()) {

                            save_user_to_database(uid);
                        } else {
                            Toast.makeText(link_with_existing_account.this, "Failed to Create User", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    onPostExecute();
                                    Log.e("Unsuccessful User", "Try again later", task.getException().getCause());
                                    Intent intent = new Intent(link_with_existing_account.this, welcome_screen.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                    }
                });
    }

    //store on database
    private void save_user_to_database(String uid) {
        UserInfo userInfo = new UserInfo(username,userEmail,userPhoneNumber);
        databaseReference.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    onPostExecute();
                    Toast.makeText(link_with_existing_account.this, "User is Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(link_with_existing_account.this,main_screen.class);
                    startActivity(intent);
                    finish();
                }else {
                    onPostExecute();
                    Log.e("Unsuccessful User", "Try again later", task.getException().getCause());
                    Intent intent = new Intent(link_with_existing_account.this, welcome_screen.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //onClick checks values entered
    public void link_now(View view) {

        userEmail = email.getText().toString();
        userPassword = password.getText().toString();
        username = name.getText().toString();

        if (!username.equals("")) {
            if (!userEmail.equals("")) {
                if (!userPassword.equals("")) {
                    link_with_email(userEmail, userPassword);
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

    //link with email
    private void link_with_email(String userEmail, String userPassword) {

        credential = EmailAuthProvider.getCredential(userEmail, userPassword);

        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        onPreExecute();
                        if (task.isSuccessful()) {
                            Log.d("newSuccess", "linkWithCredential:success");
                            updateUI();
                        } else {
                            Log.w("newFailure", "linkWithCredential:failure", task.getException());
                            Toast.makeText(link_with_existing_account.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
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