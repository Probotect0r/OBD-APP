package com.example.sagar.myapplication.EngineLoad;

import com.example.sagar.myapplication.RPM.RpmMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sagar on 11/30/2017.
 */

public interface EngineLoadService {
    @GET("rpm")
    Call<List<LoadMessage>> rpmList();

    @GET("rpm/latest")
    Call<LoadMessage> latestRpm();
}
