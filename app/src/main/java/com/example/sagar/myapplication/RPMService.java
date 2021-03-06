package com.example.sagar.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sagar on 11/26/17.
 */

public interface RPMService {
    @GET("rpm")
    Call<List<RpmMessage>> rpmList();

    @GET("rpm/latest")
    Call<RpmMessage> latestRpm();
}
