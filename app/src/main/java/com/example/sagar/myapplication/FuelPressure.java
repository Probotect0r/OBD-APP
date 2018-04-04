package com.example.sagar.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sagar.myapplication.model.ProcessedMessage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelPressure extends AppCompatActivity {

    private LineChart chart;
    private LineDataSet lineDataSet;
    private LineData lineData;

    private List<ProcessedMessage> messages;

    private final String KEY = "FUEL_PRESSURE";
    private static final String TAG = "RpmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_pressure);

        chart = findViewById(R.id.fpChart);
        setupChart();
        getLastestMessages();
    }


    private void getLastestMessages() {
        Callback<List<ProcessedMessage>> callback = new Callback<List<ProcessedMessage>>() {
            @Override
            public void onResponse(Call<List<ProcessedMessage>> call, Response<List<ProcessedMessage>> response) {
                messages = response.body();
                loadChart();
            }

            @Override
            public void onFailure(Call<List<ProcessedMessage>> call, Throwable t) {
                Log.e(TAG, "Error", t);
            }
        };

        String driveId = getIntent().getStringExtra("driveId");
        Log.d(TAG, "DriveId: " + driveId);
        Call call;
        if (driveId == null) {
            call = RestHelper.getLastTenMessages();
        } else {
            call = RestHelper.getDataByDrive(driveId);
        }

        call.enqueue(callback);
    }

    public void setupChart() {

        Description description = new Description();
        description.setText("");

        chart.setDragEnabled(true);
        chart.setPinchZoom(true);
        chart.setDescription(description);
        chart.getLegend().setEnabled(false);


        ArrayList<Entry> values = new ArrayList<Entry>();
        values.add(new Entry(0,0));

        lineDataSet = new LineDataSet(values, "MAF Data");
        lineDataSet.setLineWidth(3);
        lineDataSet.setValueTextSize(0);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setCircleRadius(4);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawCircles(false);

        lineData = new LineData(lineDataSet);
        chart.setData(lineData);
    }

    private void loadChart() {
        lineDataSet.clear();
        for(int i = 0; i < messages.size(); i++) {
            ProcessedMessage message = messages.get(i);
            Double val = (Double) message.getValues().get(KEY);
            lineDataSet.addEntry(new Entry(i, val.floatValue()));
        }

        redrawChart();
    }

    private void redrawChart() {
        chart.post(() -> {
            lineData.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
    }
}
