package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    Timer timer;
    Animation animation;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo = (ImageView) findViewById(R.id.logo);
        animation = (Animation) AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        logo.startAnimation(animation);
        timer = new Timer();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, AvatarPage.class);
                    startActivity(intent);
                    finish();
                }
            },3000);

        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            if(user != null){
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, MainDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                },3000);
            }else {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, login.class);
                        startActivity(intent);
                        finish();
                    }
                },3000);
            }
        }
    }
}