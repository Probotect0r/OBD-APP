package com.example.sagar.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class FuelActivity extends AppCompatActivity {

    private static final String TAG = "FuelActivity";
    LineChart chart;


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
        LineDataSet dataSet = new LineDataSet(yValues, "Data");
        dataSet.setLineWidth(3);
        dataSet.setValueTextSize(0);

        dataSet.setCircleColor(Color.BLACK);
        dataSet.setCircleRadius(4);
        dataSet.setCircleHoleRadius(3);

        dataSet.setHighLightColor(Color.RED);

        //add line to the grpah
        ArrayList <ILineDataSet>  dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        //complete data preparation for graph
        LineData data = new LineData(dataSets);

        //set data to the chart
        chart.setData(data);

    }
    //return back to the home dash
    public void backToHome(View view) {
        finish();
    }
}
