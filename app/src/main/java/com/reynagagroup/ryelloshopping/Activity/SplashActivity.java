package com.reynagagroup.ryelloshopping.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reynagagroup.ryelloshopping.R;

public class SplashActivity extends AppCompatActivity {

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
//                Intent i = new Intent(SplashActivity.this,RegisterActivity.class);
//                startActivity(i);
//
//                SplashActivity.this.finish();
//            }
//            public  void  finish(){
//                SplashActivity.this.finish();
//            }
//        },splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                    MainActivity.showCart =false;
                    startActivity(mainIntent);
                    finish();
            }
            public  void  finish(){
                SplashActivity.this.finish();
            }
        },splash);
    }


}
