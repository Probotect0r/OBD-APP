package com.example.sagar.myapplication;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.sagar.myapplication.model.Drive;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


public class HistoricalFragment extends Fragment {

    List<Drive> recentDrives;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public HistoricalFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historical, container, false);

        final Button[] btnDate = {view.findViewById(R.id.selectDate)};
        Button searchDrives = view.findViewById(R.id.searchDriveByDate);
        TextView txtDate = view.findViewById(R.id.txtDate);

        //RecyclerView for recent Drives
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        fetchRecentDrives();

        //setup datepicker for date  queries
        DatePickerDialog.OnDateSetListener dateSetListener;
        TimePickerDialog.OnTimeSetListener timeSetListener;

        final String[] date = {""};

        //Initialize dateSet Listener
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //parameters store latest dates
                //*** month array starts from 0 by default
                month = month + 1;
                date[0] = day + "/" + month + "/" + year;
                txtDate.setText(date[0]);
            }
        };

        btnDate[0].setOnClickListener(view1 -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(),
                    android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                    dateSetListener, year, month, day);
            dialog.getWindow();
            dialog.show();
        });

        searchDrives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DrivesByDate.class);
                i.putExtra("date", date[0]);
                startActivity(i);
                Log.d(TAG, "onClick: Searching by Date");
            }
        });

        return view;
    }

    private void fetchRecentDrives() {
        Call<List<Drive>> call = RestHelper.getRecentDrives();
        call.enqueue(new Callback<List<Drive>>() {
            @Override
            public void onResponse(Call<List<Drive>> call, Response<List<Drive>> response) {
                recentDrives = response.body();
                loadDrivesIntoRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Drive>> call, Throwable t) {
                Log.e(TAG, "Error sending message to server: " + t);
            }
        });

    }

    private void loadDrivesIntoRecyclerView() {
        adapter = new HomeCardAdapter(recentDrives);
        recyclerView.setAdapter(adapter);
    }

}
