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
    private Retrofit retrofit;
    private RetrieveService retrieveService;

    private static final String TAG = "MY_APP_DEBUG_TAG";
    List<ProcessedMessage> processedMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_drive_info);
        String driveId = this.getIntent().getStringExtra("driveId");
        setupRetrofit();
        retrieveData(driveId);
    }

    public void backToHome(View view) {
        finish();
    }

    private void setupRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.retrieveService = retrofit.create(RetrieveService.class);
    }

    private void retrieveData(String driveId) {
        Call<List<ProcessedMessage>> call = this.retrieveService.getData(driveId);
        call.enqueue(new Callback<List<ProcessedMessage>>() {
            @Override
            public void onResponse(Call<List<ProcessedMessage>> call, Response<List<ProcessedMessage>> response) {
                processedMessages = response.body();
                Log.d(TAG, "Got drive data: " + processedMessages.size());
            }

            @Override
            public void onFailure(Call<List<ProcessedMessage>> call, Throwable t) {
                Log.e(TAG, "Error creating drive: ", t);
            }
        });

    }

    public void showSpeedActivity(View view) {
        Intent showFuelActivity = new Intent (this, SpeedRPMActivity.class);
        startActivity(showFuelActivity);
    }

    public void showThrottlePosition (View view) {
        Intent showThrottlePosition = new Intent (this, ThrottlePosition.class);
        startActivity(showThrottlePosition);
    }

    public void showRpmActivity(View view) {
        Intent showRPMActivity = new Intent (this, RpmActivity.class);
        startActivity(showRPMActivity);
    }

    public void showTempActivity(View view) {
        Intent showTempActivity = new Intent (this, TemperatureActivity.class);
        startActivity(showTempActivity);
    }

    public void showFuelPressure (View view) {
        Intent showFuelPressure = new Intent (this, FuelPressure.class);
        startActivity(showFuelPressure);
    }

    public void showMAF (View view) {
        Intent showMAF = new Intent (this, MassAirFlow.class);
        startActivity(showMAF);
    }

    public void loadErrorPage(View view) {
        Intent i = new Intent(this, ViewErrorCodes.class);
        startActivity(i);
    }
}
