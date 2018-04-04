package com.example.sagar.myapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.sagar.myapplication.model.Drive;
import com.example.sagar.myapplication.model.RawMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sagar on 11/25/17.
 */

public class BluetoothThread implements Runnable {
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

    private Drive drive;

    public static final String API_ADDRESS = "192.168.0.193";
//    public static final String API_ADDRESS = "138.197.167.62";


    private static final Map<String, String> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("RPM", "010C\r");
        COMMANDS.put("THROTTLE_POSITION", "0111\r");
        COMMANDS.put("ENGINE_LOAD", "0104\r");
        COMMANDS.put("SPEED", "010D\r");
        COMMANDS.put("COOLANT_TEMPERATURE", "0105\r");
        COMMANDS.put("FUEL_PRESSURE", "010A\r");
        COMMANDS.put("MAF", "0110\r");
        COMMANDS.put("FUEL_SYSTEM_STATUS", "0103\r");
    }

    public BluetoothThread(BluetoothDevice device) {
        this.bluetoothDevice = device;
        setupRetrofit();
    }

    private void setupRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.postService = retrofit.create(PostService.class);
    }

    public void run() {
        Log.d(TAG, "BluetoothThread started.");
        setupBluetoothSocket();
        setupInputStream();
        setupOutputStream();
        connectToSocket();
        createNewDrive();
    }

    private void setupBluetoothSocket() {
        Log.d(TAG, "Setting up Bluetooth.");
        try {
            this.bluetoothSocket = this.bluetoothDevice.createRfcommSocketToServiceRecord(SERIAL_UUID);
        } catch (IOException err) {
            Log.e(TAG, "Error creating socket", err);
        }
    }

    private void setupInputStream() {
        try {
            this.inputStream = this.bluetoothSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
    }

    private void setupOutputStream() {
        try {
            this.outputStream = this.bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }
    }

    private void connectToSocket() {
        try {
            this.bluetoothSocket.connect();
            Log.d(TAG, "Connected to OBD.");
        } catch (IOException err) {
            String errorMessage= "Error connecting to device";
            Log.e(TAG, errorMessage, err);
            throw new RuntimeException(errorMessage, err);
        }
    }

    private void createNewDrive() {
        Log.d(TAG, "Creating new Drive.");
        Call<Drive> call = this.postService.createDrive();
        drive = executeCall(call);
        Log.d(TAG, "Created drive with id: " + drive.getId());
        pollDevice();
    }

    private void pollDevice() {
        while(this.continuePolling) {
            this.executeCommandsOnSystem();
            this.sleep();
        }
        closeSocket();
        endDrive();
    }

    private void executeCommandsOnSystem() {
        RawMessage rawMessage = new RawMessage();
        rawMessage.setDriveId(this.drive.getId());

        for (String commandKey : COMMANDS.keySet()) {
            writeCommand(COMMANDS.get(commandKey));
            String response = getStringFromInputStream();
            Log.d(TAG, commandKey + ": " + response);
            rawMessage.addMessageValue(commandKey, response);
        }

        sendMessageToServer(rawMessage);
    }

    private void writeCommand(String command) {
        try {
            outputStream.write(command.getBytes());
            flushOutputstream();
        } catch (IOException e) {
            Log.e(TAG, "Error writing bytes to output stream", e);
        }
    }

    private String getStringFromInputStream() {
        StringBuilder response = new StringBuilder();

        while (true) {
            byte data = getByteFromInputStream();
            char character = (char) data;

            if (character == '>') {
                return response.toString().trim();
            }

            if (character != ' ') {
                response.append(character);
            }
        }
    }

    private byte getByteFromInputStream() {
        try {
            byte data = (byte) inputStream.read();
            return data;
        } catch (IOException e) {
            String errorMessage = "Error putting thread to sleeep";
            Log.e(TAG, errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private void sendMessageToServer(RawMessage rawMessage) {
        Call<RawMessage> call = this.postService.createMessage(rawMessage);
        executeCall(call);
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void flushOutputstream() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error flushing output stream", e);
        }

    }

    private <T> T executeCall(Call<T> call) {
        try {
            return call.execute().body();
        } catch (IOException e) {
            Log.e(TAG, "Could not execute call: ", e);
            return null;
        }
    }
    public boolean isContinuePolling() {
        return continuePolling;
    }

    public synchronized void setContinuePolling(boolean continuePolling) {
        this.continuePolling = continuePolling;
    }

    private void closeSocket() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing socket", e);
        }
    }

    private void endDrive() {
        Log.d(TAG, "Ending drive.");
        Call<Drive> call = this.postService.endDrive(drive.getId());
        executeCall(call);
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }
}
