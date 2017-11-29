package com.example.sagar.myapplication.RPM;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sagar on 11/26/17.
 */

public interface RPMService {
    @GET("rpm/list")
    Call<List<RPM>> rpmList();

    @GET("rpm/list/latest")
    Call<RPM> latestRpm();
}
