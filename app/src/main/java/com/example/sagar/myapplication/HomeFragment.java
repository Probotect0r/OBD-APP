package com.example.sagar.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sagar.myapplication.model.Drive;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by achal on 2018-03-07.
 */

public class HomeFragment extends Fragment {
    private Retrofit retrofit;
    private RetrieveService retrieveService;

    List<Drive> recentDrives;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

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


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        setupRetrofit();
        fetchRecentDrives();

        return view;
    }

    private void setupRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.retrieveService = retrofit.create(RetrieveService.class);
    }

    private void fetchRecentDrives() {
        Call<List<Drive>> call = this.retrieveService.getRecentDrives();
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
