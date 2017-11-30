package com.example.sagar.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class TemperatureActivity extends AppCompatActivity {

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temprature);

        chart = (BarChart) findViewById(R.id.engineLoadChart);

        BARENTRY = new ArrayList<>();

        BarEntryLabels = new ArrayList<String>();

        AddValuesToBARENTRY();
//        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, "Projects");

        BARDATA = new BarData(Bardataset);

        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setData(BARDATA);

        chart.animateY(3000);

    }

    public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(0, 0));
        BARENTRY.add(new BarEntry(1, 1));
        BARENTRY.add(new BarEntry(2, 2));
        BARENTRY.add(new BarEntry(3, 3));
        BARENTRY.add(new BarEntry(4, 4));
        BARENTRY.add(new BarEntry(5, 5));

    }

//    public void AddValuesToBarEntryLabels(){
//
//        BarEntryLabels.add("January");
//        BarEntryLabels.add("February");
//        BarEntryLabels.add("March");
//        BarEntryLabels.add("April");
//        BarEntryLabels.add("May");
//        BarEntryLabels.add("June");
//
//    }
    
    //Return back to home screen
    public void backToHome(View view) {
        finish();
    }
}

