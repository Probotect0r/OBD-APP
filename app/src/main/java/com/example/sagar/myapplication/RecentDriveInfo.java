package com.example.sagar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RecentDriveInfo extends AppCompatActivity {
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private String driveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_drive_info);
        driveId = getIntent().getStringExtra("driveId");
    }

    public void backToHome(View view) {
        finish();
    }


    public void showSpeedActivity(View view) {
        Intent showFuelActivity = new Intent (this, LineChartActivity.class);
        showFuelActivity.putExtra("key", "SPEED");
        showFuelActivity.putExtra("title", "Speed");
        showFuelActivity.putExtra("xAxis", "Seconds");
        showFuelActivity.putExtra("yAxis", "KM/H");
        startActivity(showFuelActivity);
    }

    public void showThrottlePosition (View view) {
        Intent showThrottlePosition = new Intent (this, LineChartActivity.class);
        showThrottlePosition.putExtra("key", "THROTTLE_POSITION");
        showThrottlePosition.putExtra("title", "Throttle Position");
        showThrottlePosition.putExtra("xAxis", "Seconds");
        showThrottlePosition.putExtra("yAxis", "%");
        startActivity(showThrottlePosition);
    }

    public void showRpmActivity(View view) {
        Intent showRPMActivity = new Intent (this, LineChartActivity.class);
        showRPMActivity.putExtra("key", "RPM");
        showRPMActivity.putExtra("title", "RPM");
        showRPMActivity.putExtra("xAxis", "Seconds");
        showRPMActivity.putExtra("yAxis", "RPM");
        startActivity(showRPMActivity);
    }

    public void showTempActivity(View view) {
        Intent showTempActivity = new Intent (this, LineChartActivity.class);
        showTempActivity.putExtra("key", "COOLANT_TEMPERATURE");
        showTempActivity.putExtra("title", "Coolant Temprature");
        showTempActivity.putExtra("xAxis", "Seconds");
        showTempActivity.putExtra("yAxis", "50&#x2103;C");
        startActivity(showTempActivity);
    }


    public void showFuelPressure (View view) {
        Intent showFuelPressure = new Intent (this, LineChartActivity.class);
        showFuelPressure.putExtra("key", "FUEL_PRESSURE");
        showFuelPressure.putExtra("title", "Fuel Pressure");
        showFuelPressure.putExtra("xAxis", "Seconds");
        showFuelPressure.putExtra("yAxis", "KPa");
        startActivity(showFuelPressure);
    }

    public void showMAF (View view) {
        Intent showMAF = new Intent (this, LineChartActivity.class);
        showMAF.putExtra("key", "MAF");
        showMAF.putExtra("title", "Mass Air Flow");
        showMAF.putExtra("xAxis", "Seconds");
        showMAF.putExtra("yAxis", "g/s");
        startActivity(showMAF);
    }
}
