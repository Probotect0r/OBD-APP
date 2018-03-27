package com.example.sagar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by achal on 2018-03-07.
 */

public class Home_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        Button btn = view.findViewById(R.id.startStop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start = new Intent(getActivity(), HomeActivityStarted.class);
                startActivity(start);
            }
        });

        RecyclerView recyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;

        //Dummy Values
        String date = "26032018", duration = "15";
        int da, du;

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<RecentDriveCard> input = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            //TODO: Connect to the API and call in date and duration for 5 recent drives

            //Send in two strings date and duration
            RecentDriveCard obj = new RecentDriveCard(date, duration);
            input.add(obj);

            //TODO: Remove when getting values from API lines 58-63
            da = Integer.parseInt(date);
            du = Integer.parseInt(duration);
            da++;
            du++;
            date = Integer.toString(da);
            duration = Integer.toString(du);

        }// define an adapter

        mAdapter = new HomeCardAdapter(input);
        recyclerView.setAdapter(mAdapter);
        ///////////////

        return view;
    }
}
