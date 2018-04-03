package com.example.sagar.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MassAirFlow extends AppCompatActivity {
    private static final String TAG = "MY_APP_DEBUG_TAG";

    private TextView avg, median;
    private LineChart chart;
    private LineDataSet lineDataSet;
    private LineData lineData;

    private List<ProcessedMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_air_flow);

        avg = findViewById(R.id.txtAvgValue);
        median = findViewById(R.id.txtMedianVal);
        chart = findViewById(R.id.mafChart);

        setupChart();
        getLastestMessages();
    }

    private void getLastestMessages() {
        Call<List<ProcessedMessage>> call = RestHelper.getLastTenMessages();

        call.enqueue(new Callback<List<ProcessedMessage>>() {
            @Override
            public void onResponse(Call<List<ProcessedMessage>> call, Response<List<ProcessedMessage>> response) {
                messages = response.body();
                Log.d(TAG, "Got messages: " + messages.size());
                loadChart();
            }

            @Override
            public void onFailure(Call<List<ProcessedMessage>> call, Throwable t) {
            }
        });
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
            Double val = (Double) message.getValues().get("MAF");
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
