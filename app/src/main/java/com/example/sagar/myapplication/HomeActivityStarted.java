package com.example.sagar.myapplication;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

public class HomeActivityStarted extends AppCompatActivity {

    private Chronometer clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_started);

        clock = findViewById(R.id.chronometer2);
        startClock();
    }

    public void startClock () {
        clock.start();
    }

    public void stopDrive(View view) {
        //Grabs the ellapsed time in milliseconds
        long elapsedMills = SystemClock.elapsedRealtime() - clock.getBase();
        Log.d("HOME ACTIVITY STARTED", "stopDrive: " + elapsedMills);
        clock.stop();
        clock.setBase(SystemClock.elapsedRealtime());
        finish();
    }
}
