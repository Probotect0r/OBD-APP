package com.example.sagar.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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
            }

            @Override
            public void onFailure(Call<List<ProcessedMessage>> call, Throwable t) {
                Log.e(TAG, "Error creating drive: ", t);
            }
        });

    }
}
