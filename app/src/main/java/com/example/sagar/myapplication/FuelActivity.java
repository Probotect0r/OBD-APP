package com.example.sagar.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class FuelActivity extends AppCompatActivity {

    private static final String TAG = "FuelActivity";
    LineChart chart;

    private LineDataSet dataSet;
    private LineData data;

    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);

        chart = findViewById(R.id.lineChart);

        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);

        //create coordinates for the Graph
        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60));
        yValues.add(new Entry(1, 70));
        yValues.add(new Entry(2, 80));
        yValues.add(new Entry(3, 60));
        yValues.add(new Entry(4, 70));
        yValues.add(new Entry(5, 50));
        yValues.add(new Entry(6, 60));

        //Line for connecting the dots and design
        dataSet = new LineDataSet(yValues, "Data");
        dataSet.setLineWidth(3);
        dataSet.setValueTextSize(0);
        dataSet.setDrawFilled(true);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setCircleRadius(4);
        dataSet.setCircleHoleRadius(3);

        dataSet.setHighLightColor(Color.RED);

        //add line to the grpah
        ArrayList <ILineDataSet>  dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        //complete data preparation for graph
        data = new LineData(dataSets);

        //draw data to the chart
        chart.setData(data);
        chart.animateX(2000, Easing.EasingOption.EaseInCubic);

        Thread pollingThread = new Thread() {

            @Override
            public void run() {
                System.out.println("Started polling thread.");
                int[] values = new int[]{80, 90, 85, 110, 120, 130, 110, 100, 95, 105};
                for (int i = 0; i < 7; i++) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dataSet.removeEntry(0);
                    dataSet.addEntry(new Entry(i + 7, values[i]));
                    chart.post(new Runnable() {
                        @Override
                        public void run() {
                            data.notifyDataChanged();
                            chart.notifyDataSetChanged();
                            chart.invalidate();
                        }
                    });

                }
            }
        };

        pollingThread.start();
    }
    //return back to the home dash
    public void backToHome(View view) {
        finish();
    }
}
