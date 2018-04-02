package com.example.sagar.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class FuelPressure extends AppCompatActivity {

    LineChart fpChart;
    private LineDataSet lineDataSet;
    private LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_pressure);

        fpChart = findViewById(R.id.fpChart);

        showChartData();
    }

    public void showChartData() {
        Description description = new Description();
        description.setText("");

        //chart Design
        fpChart.setPinchZoom(true);
        fpChart.setDragEnabled(true);
        fpChart.setScaleEnabled(true);
        fpChart.setDescription(description);
        fpChart.getLegend().setEnabled(false);

        //Add XY Coordinates
        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0 , 1));
        yValues.add(new Entry(1 , 3));
        yValues.add(new Entry(2 , 5));

        //SetUp line Design
        lineDataSet = new LineDataSet(yValues, "data set 1");
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

        //show Line Data
        fpChart.setData(lineData);
    }

    public void backToHome(View view) {
        finish();
    }
}
