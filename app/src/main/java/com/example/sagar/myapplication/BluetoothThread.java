package com.example.sagar.myapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by sagar on 11/25/17.
 */

public class BluetoothThread extends Thread {
    // The UUID of the SerialPort bluetooth service
    UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private static final String TAG = "MY_APP_DEBUG_TAG";
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;

    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean continuePolling = true;

    private Retrofit retrofit;
    private PostService postService;

    public static final String API_ADDRESS = "192.168.0.17";
//    public static final String API_ADDRESS = "138.197.167.62";

    private static final List<String> COMMANDS = new ArrayList<>(Arrays.asList("010C\r", "0111\r", "010D\r"));

    public BluetoothThread(BluetoothDevice device) {
        this.bluetoothDevice = device;

        // Set up Rest Service
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://" + this.API_ADDRESS + ":8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.postService = retrofit.create(PostService.class);
    }

    public void run() {
        // Set up socket
        try {
            this.bluetoothSocket = this.bluetoothDevice.createRfcommSocketToServiceRecord(SERIAL_UUID);
        } catch (IOException err) {
            Log.e(TAG, "Error creating socket", err);
        }

        // Get the input and output streams
        try {
            this.inputStream = this.bluetoothSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            this.outputStream = this.bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        // Connect to socket
        try {
            this.bluetoothSocket.connect();
            Log.d(TAG, "Connected to OBD.");
        } catch (IOException err) {
            Log.e(TAG, "Error connecting to device", err);
            return;
        }

        // Start polling the device
        while(this.continuePolling) {
            // Loop through each command
            for (String command : COMMANDS) {

                // Write the command to the sensor
                try {
                    this.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error writing data: ", e);
                    break;
                }

                // Get the response
                String message = null;
                try {
                    message = this.getString();
                } catch (IOException e) {
                    Log.e("Error rawdata: ", e.toString());
                    break;
                }
//                Log.d(TAG, "Message from bluetooth: " + message);


                // Send the message to the server
                RawMessage rawMessage = new RawMessage(message);
                Call<RawMessage> call = this.postService.createMessage(rawMessage);
                call.enqueue(new Callback<RawMessage>() {
                    @Override
                    public void onResponse(Call<RawMessage> call, Response<RawMessage> response) {}

                    @Override
                    public void onFailure(Call<RawMessage> call, Throwable t) {
                        Log.e(TAG, "Error sending message to server: " + t);
                    }
                });

            }

            // Sleep for a bit
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error putting thread to sleeep: " + e);
                e.printStackTrace();
            }
        }
    }

    public String getString() throws IOException {
        StringBuilder res = new StringBuilder();

        // Keep reading from the InputStream until the end of the message is reached
        while (true) {
                // Read one byte from the InputStream
                byte b = (byte) inputStream.read();

                if (((char) b) == '>') {
                    return res.toString().trim();
                }

                if (((char) b) != ' ') {
                    res.append((char) b);
                }
        }
    }

    public void write(byte[] bytes) throws  IOException {
            outputStream.write(bytes);
            outputStream.flush();
    }

    public boolean isContinuePolling() {
        return continuePolling;
    }

    public synchronized void setContinuePolling(boolean continuePolling) {
        this.continuePolling = continuePolling;
    }
}
