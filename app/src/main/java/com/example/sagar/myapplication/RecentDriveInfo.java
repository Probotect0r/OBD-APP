package com.example.sagar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.sagar.myapplication.model.ProcessedMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Intent showFuelActivity = new Intent (this, SpeedRPMActivity.class);
        showFuelActivity.putExtra("driveId", driveId);
        startActivity(showFuelActivity);
    }


    public void showThrottlePosition (View view) {
        Intent showThrottlePosition = new Intent (this, ThrottlePosition.class);
        showThrottlePosition.putExtra("driveId", driveId);
        startActivity(showThrottlePosition);
    }

    public void showRpmActivity(View view) {
        Intent showRPMActivity = new Intent (this, RpmActivity.class);
        showRPMActivity.putExtra("driveId", driveId);
        startActivity(showRPMActivity);
    }

    public void showTempActivity(View view) {
        Intent showTempActivity = new Intent (this, TemperatureActivity.class);
        showTempActivity.putExtra("driveId", driveId);
        startActivity(showTempActivity);
    }

    public void showFuelPressure (View view) {
        Intent showFuelPressure = new Intent (this, FuelPressure.class);
        showFuelPressure.putExtra("driveId", driveId);
        startActivity(showFuelPressure);
    }

    public void showMAF (View view) {
        Intent showMAF = new Intent (this, MassAirFlow.class);
        showMAF.putExtra("driveId", driveId);
        startActivity(showMAF);
    }

    public void loadErrorPage(View view) {
        Intent i = new Intent(this, ViewErrorCodes.class);
        i.putExtra("driveId", driveId);
        startActivity(i);
    }
}
