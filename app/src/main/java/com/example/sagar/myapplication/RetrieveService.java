package com.example.sagar.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sagar on 3/27/18.
 */

public interface RetrieveService {
    @GET("drive/recent")
    Call<List<Drive>> getRecentDrives();

    @GET("data/{driveId}")
    Call<List<ProcessedMessage>> getData(@Path("driveId") String driveId);

}