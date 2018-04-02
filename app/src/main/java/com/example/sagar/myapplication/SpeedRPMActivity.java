package com.example.sagar.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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

public class SpeedRPMActivity extends AppCompatActivity {
    private LineChart lineChart;
    private LineDataSet lineDataSet;
    private LineData lineData;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    RPMService service = retrofit.create(RPMService.class);

    private RPMService rpmService;
    private Thread pollingThread;
    private boolean continuePolling = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_rpm);

        // Set up chart
        lineChart = findViewById(R.id.speedLineChart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setVisibleXRangeMaximum(10);

        rpmService = retrofit.create(RPMService.class);

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
                Log.e("Speed Activity", "Error in request to /rpm" + t.getMessage());
                t.printStackTrace();
                // TODO: Show some error
            }
        });
    }



    public void backToHome(View view) {
        finish();
    }

    public void onStop() {
        super.onStop();
        this.continuePolling = false;
//        String result = "";

//        NotificationCompat.Builder notif = new NotificationCompat.Builder(this)
//                .setContentTitle("Vehicle Analytics Bruh")
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(result));
//
//
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(1, notif.build());
    }
}
