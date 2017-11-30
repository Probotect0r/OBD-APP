package com.example.sagar.myapplication.RPM;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.sagar.myapplication.BluetoothThread;
import com.example.sagar.myapplication.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RpmActivity extends AppCompatActivity {

    private static final String TAG = "RpmActivity";

    private LineChart lineChart;
    private LineDataSet lineDataSet;
    private LineData lineData;

    private RPMService rpmService;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
                .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private Thread pollingThread;
    private boolean continuePolling = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpm);

        // Set up RpmService
        rpmService = retrofit.create(RPMService.class);

        // Set up chart
        lineChart = findViewById(R.id.lineChart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setVisibleXRangeMaximum(10);

        // Create list of chart entries
        ArrayList<Entry> yValues = new ArrayList<>();

        // Set up the polling thread
        pollingThread = new Thread(() -> {
            System.out.println("Started polling thread.");
            while (continuePolling) {
                // Make the API call to retrieve latest RPM
                Call<RpmMessage> call = this.rpmService.latestRpm();
                call.enqueue(new Callback<RpmMessage>() {

                    @Override
                    public void onResponse(Call<RpmMessage> call, Response<RpmMessage> response) {
                        RpmMessage msg = response.body();
                        lineDataSet.addEntry(new Entry(yValues.size(), msg.getRpm()));
                        lineChart.post(() -> {
                            lineData.notifyDataChanged();
                            lineChart.notifyDataSetChanged();
                            lineChart.invalidate();
                        });
                    }

                    @Override
                    public void onFailure(Call<RpmMessage> call, Throwable t) {

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

        // 1. Query last 10 points (Done)
        // 2. Display those points on graph (Done)
        // 3. Start polling thread to get latest message on an interval (Done)
        // 4. Update the graph with latest message (done)


        // Request the 10 latest rpm messages from the server
        Call<List<RpmMessage>> call = this.rpmService.rpmList();
        call.enqueue(new Callback<List<RpmMessage>>() {
            @Override
            public void onResponse(Call<List<RpmMessage>> call, Response<List<RpmMessage>> response) {
                // Get the response and set up the chart
                List<RpmMessage> messages = response.body();
                System.out.println("Looping through received messages");
                for (int i = 0; i < messages.size(); i++) {
                    RpmMessage msg = messages.get(i);
                    yValues.add(new Entry(i, msg.getRpm()));
                }

                // Create line data set and add styling
                lineDataSet = new LineDataSet(yValues, "RPM");
                lineDataSet.setLineWidth(3);
                lineDataSet.setValueTextSize(0);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setCircleColor(Color.BLACK);
                lineDataSet.setCircleRadius(4);
                lineDataSet.setCircleHoleRadius(3);
                lineDataSet.setHighLightColor(Color.RED);
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setDrawCircles(false);

                // add line to the graph
                lineData = new LineData(lineDataSet);

                //draw data to the chart
                lineChart.setData(lineData);
                lineChart.animateX(2000, Easing.EasingOption.EaseInCubic);
                lineChart.invalidate();

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
            public void onFailure(Call<List<RpmMessage>> call, Throwable t) {
                Log.e(TAG, "Error in request to /rpm" + t.getMessage());
                t.printStackTrace();
                // TODO: Show some error
            }
        });
    }

    //return back to the home dash
    public void backToHome(View view) {
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.continuePolling = false;
    }
}
