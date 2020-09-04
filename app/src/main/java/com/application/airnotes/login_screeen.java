package com.application.airnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class login_screeen extends AppCompatActivity {

    Button login_btn;
    EditText email_id,password;
    FirebaseAuth firebaseAuth;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screeen);

        login_btn = findViewById(R.id.login_btn);
        email_id = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);

    }

    public void to_register_screen(View view) {
        Intent intent = new Intent(this,register_screen.class);
        startActivity(intent);
    }

    public void to_welcome_screen(View view) {
        Intent intent = new Intent(this,welcome_screen.class);
        startActivity(intent);
    }

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

    private void loginUser(String userMailId, String userPassword) {
        onPreExecute();
        firebaseAuth.signInWithEmailAndPassword(userMailId,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    onPostExecute();
                    Toast.makeText(login_screeen.this, "Logged In Successfully. ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(login_screeen.this,main_screen.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(login_screeen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
