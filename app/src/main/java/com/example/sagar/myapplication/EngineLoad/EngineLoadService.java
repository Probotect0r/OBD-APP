package com.example.sagar.myapplication.EngineLoad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sagar on 11/30/2017.
 */

public interface EngineLoadService {
    @GET("load")
    Call<List<LoadMessage>> loadList();

    @GET("load/latest")
    Call<LoadMessage> latestLoad();
}
