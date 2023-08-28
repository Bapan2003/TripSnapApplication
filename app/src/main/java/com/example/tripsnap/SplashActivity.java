package com.example.tripsnap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences= getSharedPreferences("login", Context.MODE_PRIVATE);
                if (sharedPreferences.contains("isUserLogIn")) {
                    Intent intent = new Intent(SplashActivity.this, BusBookingActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2500);
    }
}