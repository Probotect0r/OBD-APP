package com.example.sagar.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.sagar.myapplication.model.ProcessedMessage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


public class MassAirFlow extends AppCompatActivity {

    private TextView avg, median;
    private LineChart chart;
    private List<ProcessedMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_air_flow);

        avg = findViewById(R.id.txtAvgValue);
        median = findViewById(R.id.txtMedianVal);
        chart = findViewById(R.id.mafChart);

        setupChart();

    }

    public void setupChart() {

        Description description = new Description();
        description.setText("");

        chart.setDragEnabled(true);
        chart.setPinchZoom(true);
        chart.setDescription(description);
        chart.getLegend().setEnabled(false);


        ArrayList<Entry> yValues = new ArrayList<Entry>();
        yValues.add(new Entry(0,1));

        LineDataSet set = new LineDataSet(yValues, "MAF Data");
        set.setLineWidth(3);
        set.setValueTextSize(0);
        set.setDrawFilled(true);
        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(4);
        set.setCircleHoleRadius(3);
        set.setHighLightColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawCircles(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        LineData data = new LineData(dataSets);

        chart.setData(data);
    }
}
