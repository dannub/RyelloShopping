package com.reynagagroup.ryelloshopping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reynagagroup.ryelloshopping.R;

public class SplashActivity extends AppCompatActivity {

    private int splash = 3000;

    private  FirebaseAuth firebaseAuth;


    private ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        connectivityManager =(ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


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
                if (networkInfo !=null && networkInfo.isConnected()==true) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    if (currentUser != null) {

                        FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).update("Lastseen", FieldValue.serverTimestamp())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                            MainActivity.showCart = false;
                                            startActivity(mainIntent);
                                            finish();
                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        MainActivity.showCart = false;
                        startActivity(mainIntent);
                        finish();
                    }
                }else {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    MainActivity.showCart = false;
                    startActivity(mainIntent);
                    finish();
                }

            }
            public  void  finish(){
                SplashActivity.this.finish();
            }
        },splash);
    }


}
