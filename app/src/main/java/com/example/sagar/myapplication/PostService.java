package com.example.sagar.myapplication;

import com.example.sagar.myapplication.model.Drive;
import com.example.sagar.myapplication.model.RawMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostService {
    @POST("rawmessage")
    Call<RawMessage> createMessage(@Body RawMessage rawMessage);

    @GET("drive/new")
    Call<Drive> createDrive();

    @PUT("drive/end/{driveId}")
    Call<Drive> endDrive(@Path("driveId") String driveId);
}
