package com.example.sagar.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by achal on 2018-03-07.
 */

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        Button btn = view.findViewById(R.id.startStop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                    .beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.fade_out)
                    .replace(R.id.fragment_content, new Dash_fragment())
                    .commit();
            }
        });

        return view;
    }
}
