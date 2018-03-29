package com.example.sagar.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

/**
 * Created by achal on 2018-03-07.
 */

public class Dash_fragment extends Fragment{

    private Chronometer clock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dash_fragment, null);
        clock = view.findViewById(R.id.chronometer2);
        startClock();
        return view;
    }

    public void startClock () {
        clock.start();
    }

//    public void stopDrive(View view) {
//        //Grabs the ellapsed time in milliseconds
//        long elapsedMills = SystemClock.elapsedRealtime() - clock.getBase();
//        Log.d("HOME ACTIVITY STARTED", "stopDrive: " + elapsedMills);
//        clock.stop();
//        clock.setBase(SystemClock.elapsedRealtime());
//    }

}
