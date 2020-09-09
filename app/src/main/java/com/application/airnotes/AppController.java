package com.application.airnotes;

import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

public class AppController {

    public static DatabaseReference databaseReference;
    public static FirebaseAuth firebaseAuth;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static FirebaseUser currentUser;
    public static String uid;

}
