package com.example.sagar.myapplication;

import com.example.sagar.myapplication.model.Drive;
import com.example.sagar.myapplication.model.ProcessedMessage;
import com.example.sagar.myapplication.model.RawMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sagar on 4/2/18.
 */

public class RestHelper {
    private static RetrieveService retrieveService = new Retrofit.Builder()
                .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrieveService.class);

    private static PostService postService = new Retrofit.Builder()
            .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostService.class);

    public static Call<RawMessage> createMessage(RawMessage rawMessage) {
        return postService.createMessage(rawMessage);
    }

    public static Call<Drive> createDrive() {
        return postService.createDrive();
    }

    public static Call<Drive> endDrive(String driveId) {
        return postService.endDrive(driveId);
    }

    public static Call<List<Drive>> getRecentDrives() {
        return retrieveService.getRecentDrives();
    }

    public static Call<List<ProcessedMessage>> getDataByDrive(String driveId) {
        return retrieveService.getData(driveId);
    }

    public static Call<Drive> getLastDrive() {
        return retrieveService.getLastDrive();
    }

    public static Call<ProcessedMessage> getLatestMessage() {
        return retrieveService.getLatestMessage();
    }

    public static Call<List<ProcessedMessage>> getLastTenMessages() {
        return retrieveService.getLastTenMessages();
    }
}
