package com.reynagagroup.ryelloshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    private int splash = 3000;

    private  FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(Splash.this,RegisterActivity.class);
//                startActivity(i);
//
//                Splash.this.finish();
//            }
//            public  void  finish(){
//                Splash.this.finish();
//            }
//        },splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
            }
            public  void  finish(){
                Splash.this.finish();
            }
        },splash);
    }


}
