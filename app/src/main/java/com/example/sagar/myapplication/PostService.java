package com.example.sagar.myapplication;

import com.example.sagar.myapplication.model.Drive;
import com.example.sagar.myapplication.model.RawMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostService {
    @POST("rawmessage")
    Call<RawMessage> createMessage(@Body RawMessage rawMessage);

    @GET("drive/new")
    Call<Drive> createDrive();
}
