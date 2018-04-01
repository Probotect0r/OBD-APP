package com.example.sagar.myapplication;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by achal on 2018-03-07.
 */

public class HomeFragment extends Fragment {

    private boolean isDriving = false;
    private Button btn;
    private Chronometer clock;
    private TextView driveListTitle;
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        btn = view.findViewById(R.id.startStop);
        clock = view.findViewById(R.id.chronometerHome);
        driveListTitle = view.findViewById(R.id.txtDriveHome);
        scrollView = view.findViewById(R.id.scrollHome);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isDriving) {
                    isDriving = true;
                    btn.setText("Stop Drive");
                    driveListTitle.setText("Current Drive");
                    changeScrollViewWeight(70);
                    clock.setVisibility(view.VISIBLE);
                    startClock();

                } else {
                    isDriving = false;
                    btn.setText("Start Drive");
                    driveListTitle.setText("Recent Drive");
                    clock.setVisibility(view.GONE);
                    changeScrollViewWeight(80);
                    stopClock();
                }
            }
        });

        return view;
    }

    public void startClock () {
        clock.setBase(SystemClock.elapsedRealtime());
        clock.start();
    }

    public void stopClock () {
        clock.stop();
        clock.setBase(SystemClock.elapsedRealtime());
        long elapsedMills = SystemClock.elapsedRealtime() - clock.getBase();
    }

    public void changeScrollViewWeight (int val) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scrollView
                .getLayoutParams();
        layoutParams.weight = val;
        scrollView.requestLayout();
    }
}
