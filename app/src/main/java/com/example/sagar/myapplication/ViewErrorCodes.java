package com.example.sagar.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ViewErrorCodes extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    ArrayList<ErrorCard> errors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_error_codes);

        errors.add(new ErrorCard("P0014", "Incorrect CamShaft Timing or failed timing valve"));
        errors.add(new ErrorCard("P000F", "Fuel Pressure sensor malfunction or Fuel Volume Regulator failure"));
        errors.add(new ErrorCard("P002D", "Incorrect or contaminated oil in use."));

        recyclerView = findViewById(R.id.errorCardsRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ErrorCode_RecyclerView_adapter(errors);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

}
