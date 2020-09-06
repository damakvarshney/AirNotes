package com.application.airnotes;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppController extends Application {

    public static FirebaseDatabase database;
    public static DatabaseReference reference;
    public static FirebaseAuth firebaseAuth;
    public static String CurrentUser ;

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseAuth = firebaseAuth.getInstance();


    }
}
