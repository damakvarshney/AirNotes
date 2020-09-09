package com.application.airnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class login_screeen extends AppCompatActivity {

    Button login_btn, submit_btn;
    EditText email_id, password;
    FirebaseAuth firebaseAuth;
    DatabaseReference database_user;
    private SpotsDialog progressDialog;
    TextView enter_mail_textView, forgot_pass_textView;
    LinearLayout password_linearLayout, email_linearLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screeen);

        //findViewBy IDs
        login_btn = findViewById(R.id.login_btn);
        email_id = findViewById(R.id.editTextTextEmailAddress);

        password = findViewById(R.id.editTextTextPassword);
        enter_mail_textView = findViewById(R.id.enter_mailId_textView);
        forgot_pass_textView = findViewById(R.id.forgot_pass_textView);
        password_linearLayout = findViewById(R.id.password_layout);
        submit_btn = findViewById(R.id.submit_btn);
        email_linearLayout = findViewById(R.id.email_layout);

        firebaseAuth = FirebaseAuth.getInstance();
        database_user = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);
        sharedPreferences = getSharedPreferences("AIRNOTES_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //Login onClick : Email verification
    public void login_now(View view) {
        String userMailId = email_id.getText().toString();
        String userPassword = password.getText().toString();


        if (!userMailId.equals("")) {
            if (!userPassword.equals("")) {
                loginUser(userMailId, userPassword);
            } else {
                Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Enter Your Email Address", Toast.LENGTH_SHORT).show();
        }
    }

    //Submit onClick : forgot password
    public void reset_password(View view) {
        String after_forgot_mailId = email_id.getText().toString();

        if (!after_forgot_mailId.equals("")) {
            onPreExecute();
            firebaseAuth.sendPasswordResetEmail(after_forgot_mailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        onPostExecute();
                        Toast.makeText(login_screeen.this, "Check your emails to Reset Your Password", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login_screeen.this,login_screeen.class);
                        startActivity(intent);
                        finish();
                    } else if (task.isCanceled()) {
                        onPostExecute();
                        Toast.makeText(login_screeen.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Cancelled", Objects.requireNonNull(task.getException().getLocalizedMessage()));
                        Intent intent = new Intent(login_screeen.this,login_screeen.class);
                        startActivity(intent);
                        finish();
                    } else {
                        onPostExecute();
                        Toast.makeText(login_screeen.this, "This Email Id isn't present in the Database", Toast.LENGTH_SHORT).show();
                        Log.e("Error", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getLocalizedMessage()));
                        Intent intent = new Intent(login_screeen.this,login_screeen.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
        }
    }

   //login using email and password
    private void loginUser(String userMailId, String userPassword) {
        onPreExecute();
        firebaseAuth.signInWithEmailAndPassword(userMailId, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    editor.putBoolean("LOGIN_STATUS",true);
                    editor.commit();
                    onPostExecute();
                    Toast.makeText(login_screeen.this, "Logged In Successfully. ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(login_screeen.this, main_screen.class);
                    startActivity(intent);
                    finish();
                } else {
                    editor.putBoolean("LOGIN_STATUS",false);
                    editor.commit();
                    onPostExecute();
                    Toast.makeText(login_screeen.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Changing layout for forget password textView
    public void get_password(View view) {
        enter_mail_textView.setVisibility(View.VISIBLE);
        forgot_pass_textView.setVisibility(View.VISIBLE);
        submit_btn.setVisibility(View.VISIBLE);

        password_linearLayout.setVisibility(View.GONE);
        forgot_pass_textView.setVisibility(View.GONE);
        login_btn.setVisibility(View.GONE);
    }

    //BackArrow button
    public void to_welcome_screen(View view) {
        Intent intent = new Intent(this, welcome_screen.class);
        startActivity(intent);
    }

    //Don't have account
    public void to_register_screen(View view) {
        Intent intent = new Intent(this, register_screen.class);
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
}
