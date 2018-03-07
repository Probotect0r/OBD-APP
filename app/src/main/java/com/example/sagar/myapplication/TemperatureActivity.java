package com.example.sagar.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.sagar.myapplication.EngineLoad.EngineLoadService;
import com.example.sagar.myapplication.EngineLoad.LoadMessage;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class TemperatureActivity extends AppCompatActivity {

    BarChart chart;
    ArrayList<BarEntry> barEntries;
    BarDataSet barDataSet;
    BarData barData ;
    private static final String TAG = "TemperatureActivity";
    private EngineLoadService engineLoadService;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private Thread pollingThread;
    private boolean continuePolling = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temprature);

        // Get reference to chart
        chart = findViewById(R.id.engineLoadChart);
        Description d = new Description();
        d.setText("");
        chart.setDescription(d);

        // Set up RpmService
        engineLoadService = retrofit.create(EngineLoadService.class);

        // Set up entries for chart
        barEntries = new ArrayList<>();
        barDataSet = new BarDataSet(barEntries, "Engine Load");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        // Set up the bardata
        barData = new BarData(barDataSet);

        chart.setData(barData);
        chart.animateY(3000);

        pollingThread = new Thread(() -> {
            System.out.println("Started polling thread.");
            while (continuePolling) {
                // Make the API call to retrieve latest RPM
                Call<LoadMessage> call = this.engineLoadService.latestLoad();
                call.enqueue(new Callback<LoadMessage>() {

                    @Override
                    public void onResponse(Call<LoadMessage> call, Response<LoadMessage> response) {
                        LoadMessage msg = response.body();
                        barDataSet.addEntry(new BarEntry(barEntries.size(), msg.getLoad()));
                        chart.post(() -> {
                            barData.notifyDataChanged();
                            chart.notifyDataSetChanged();
                            chart.invalidate();
                        });
                    }

                    @Override
                    public void onFailure(Call<LoadMessage> call, Throwable t) {

                    }
                });

                // Sleep and repeat
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Request the 10 latest load messages from the server
        Call<List<LoadMessage>> call = this.engineLoadService.loadList();
        call.enqueue(new Callback<List<LoadMessage>>() {
            @Override
            public void onResponse(Call<List<LoadMessage>> call, Response<List<LoadMessage>> response) {
                // Get the response and set up the chart
                List<LoadMessage> messages = response.body();
                for (int i = 0; i < messages.size(); i++) {
                    LoadMessage msg = messages.get(i);
                    barEntries.add(new BarEntry(i, msg.getLoad()));
                }

                // Create line data set and add styling
                barDataSet = new BarDataSet(barEntries, "RPM");
                barDataSet.setValueTextSize(0);
                barDataSet.setHighLightColor(Color.RED);

                // add line to the graph
                barData = new BarData(barDataSet);

                //draw data to the chart
                chart.setData(barData);
                chart.animateX(2000, Easing.EasingOption.EaseInCubic);
                chart.invalidate();

                // Start the polling thread after sleeping for 500
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Staring polling thread.");
                pollingThread.start();
            }

            @Override
            public void onFailure(Call<List<LoadMessage>> call, Throwable t) {
                Log.e(TAG, "Error in request to /load" + t.getMessage());
                t.printStackTrace();
                // TODO: Show some error
            }
        });
    }

    //Return back to home screen
    public void backToHome(View view) {
        finish();
    }

    public void onStop() {
        super.onStop();
        this.continuePolling = false;
    }
}

