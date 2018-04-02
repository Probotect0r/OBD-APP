package com.example.sagar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.sagar.myapplication.model.Drive;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class DrivesByDate extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrieveService retrieveService;

    private String date = "";
    List<Drive> recentDrives;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_by_date);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        //date format dd/mm/yyyy
        date = getIntent().getStringExtra("date");

        recyclerView = (RecyclerView) findViewById(R.id.searchedDrive_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        setupRetrofit();
        fetchRecentDrives();
    }

    private void setupRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.retrieveService = retrofit.create(RetrieveService.class);
    }

    private void fetchRecentDrives() {

        //TODO: GET drives by specified Date instead of recentDrives
        Call<List<Drive>> call = this.retrieveService.getRecentDrives();
        call.enqueue(new Callback<List<Drive>>() {
            @Override
            public void onResponse(Call<List<Drive>> call, Response<List<Drive>> response) {
                recentDrives = response.body();
                loadDrivesIntoRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Drive>> call, Throwable t) {
                Log.e(TAG, "Error sending message to server: " + t);
            }
        });

    }

    private void loadDrivesIntoRecyclerView() {
        adapter = new HomeCardAdapter(recentDrives);
        recyclerView.setAdapter(adapter);
    }

    public void showDriveInfo(View view) {
        Intent i = new Intent (this, RecentDriveInfo.class);
        startActivity(i);
    }
}
