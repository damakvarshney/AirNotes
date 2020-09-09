package com.application.airnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.airnotes.Data.UserInfo;
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

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class register_screen extends AppCompatActivity {

    Button register_btn, verify_btn;
    EditText name, email_id, password, mobile_no;
    FirebaseAuth firebaseAuth;
    DatabaseReference database_user;
    private SpotsDialog progressDialog;
    String  user_mobile_no;
    TextView enter_mobile_txtView, signIn_Mobile_text;
    LinearLayout mobile_layout, email_layout, name_layout, password_layout;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallbacks;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        //findViewByIds
        register_btn = findViewById(R.id.register_btn);
        name = findViewById(R.id.editTextName);
        email_id = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        mobile_no = findViewById(R.id.editTextMobileRegister);
        enter_mobile_txtView = findViewById(R.id.enter_mailId_textView);
        mobile_layout = findViewById(R.id.mobile_layout);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        name_layout = findViewById(R.id.name_layout);
        signIn_Mobile_text = findViewById(R.id.signin_otp_textView);
        verify_btn = findViewById(R.id.verify_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        database_user = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);
        sharedPreferences = getSharedPreferences("AIRNOTES_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Callback for Mobile Verification
        verificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn_with_mobile(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                onPostExecute();
                Toast.makeText(register_screen.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    //verify mobile number to send call back
    private void verify_mobile_number(String user_mobile_no) {
        onPreExecute();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(user_mobile_no, 60, TimeUnit.SECONDS, this, verificationStateChangedCallbacks);

    }

    //onClick verify number entered
    public void verify_now(View view) {
        user_mobile_no = "+91" + mobile_no.getText().toString();

        if (!mobile_no.getText().toString().equals("")) {
            verify_mobile_number(user_mobile_no);
        } else {
            Toast.makeText(this, "Please enter valid Mobile No.", Toast.LENGTH_SHORT).show();
        }
    }

    //onClick register method
    public void register_now(View view) {

        String username = name.getText().toString();
        String userMailId = email_id.getText().toString();
        String userPassword = password.getText().toString();
        String userPhoneNumber = "+91" + mobile_no.getText().toString();

        if (!username.equals("")) {
            if (!userMailId.equals("")) {
                if (!userPassword.equals("")) {
                    register(username, userMailId, userPassword, userPhoneNumber);
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

    //register method to get user registered
    private void register(final String username, final String userMailId, final String userPassword, final String userPhoneNumber) {
        firebaseAuth.createUserWithEmailAndPassword(userMailId, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onPreExecute();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    final String uid = currentUser.getUid();

                    //using data class storing user to database
                    UserInfo userInfo = new UserInfo(username, userMailId, userPhoneNumber);

                    database_user.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                editor.putString("user_id_from_email",uid);
                                onPostExecute();
                                Toast.makeText(register_screen.this, "User is Registered Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(register_screen.this, "Failed to Create User", Toast.LENGTH_SHORT).show();
                                firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.e("Unsuccessful User", "Removed ", task.getException().getCause());
                                        Intent intent = new Intent(register_screen.this, welcome_screen.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    });

                } else {
                    Log.e("Unsuccessful User", "Removed", task.getException().getCause());
                    Toast.makeText(register_screen.this, "Invalid Email Address or Password", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    //register with mobile
    private void signIn_with_mobile(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onPostExecute();
                    Toast.makeText(register_screen.this, "Mobile number verified successfully", Toast.LENGTH_SHORT).show();

                    Intent intentWithData = new Intent(register_screen.this, link_with_existing_account.class);
                    intentWithData.putExtra("MOBILE_NUMBER", user_mobile_no);
                    startActivity(intentWithData);
                    finish();

                } else {
                    onPostExecute();
                    Toast.makeText(register_screen.this, "Mobile number verification failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Goto login screen through textView{Already a user}
    public void to_login_screen(View view) {
        Intent intent = new Intent(this, login_screeen.class);
        startActivity(intent);
    }

    //onClick signIn with mobile(changing layout)
    public void sign_in_using_otp(View view) {
        mobile_layout.setVisibility(View.VISIBLE);
        enter_mobile_txtView.setVisibility(View.VISIBLE);
        verify_btn.setVisibility(View.VISIBLE);

        name_layout.setVisibility(View.GONE);
        email_layout.setVisibility(View.GONE);
        password_layout.setVisibility(View.GONE);
        signIn_Mobile_text.setVisibility(View.GONE);
        register_btn.setVisibility(View.GONE);


    }

    //Go to welcome screen through imageView{backArrow}
    public void to_welcome_screen(View view) {
        Intent intent = new Intent(this, welcome_screen.class);
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