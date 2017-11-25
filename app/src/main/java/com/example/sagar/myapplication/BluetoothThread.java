package com.example.sagar.myapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by sagar on 11/25/17.
 */

public class BluetoothThread extends Thread {
    // bluetooth serial port service
    UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private static final String TAG = "MY_APP_DEBUG_TAG";
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;

    private InputStream inputStream;
    private OutputStream outputStream;

    // mmBuffer store for the stream
    private byte[] mmBuffer;


    String command = "010c\r";
    public BluetoothThread(BluetoothDevice device) {
        this.bluetoothDevice = device;

    }

    public void run() {
        // Set up connection
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            this.bluetoothSocket = this.bluetoothDevice.createRfcommSocketToServiceRecord(SERIAL_UUID);
        } catch (IOException err) {
            Log.e(TAG, "Error creating socket", err);
        }

        // Get the input and output streams; using temp objects because
        // member streams are final.
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

        try {
            this.bluetoothSocket.connect();
            Log.d(TAG, "Connected.");


        } catch (IOException err) {
            Log.e(TAG, "Error connecting to device", err);
            return;
        }

        // Start polling the device
        while(true) {
            this.write(this.command.getBytes());
            String message = this.getString();
            Log.d(TAG, message);

            // Sleep for a bit
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error putting thread to sleeep: " + e);
                e.printStackTrace();
            }
        }
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException err) {
            System.out.println("Error closing thread: " + err);
        }
    }

    public String getString() {
        StringBuilder res = new StringBuilder();

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read one byte from the InputStream
                byte b = (byte) inputStream.read();

                if (((char) b) == '>') {
                    return res.toString().trim();
                }

                if (((char) b) != ' ') {
                    res.append((char) b);
                }
            } catch (IOException e) {
                Log.e("Error rawdata: ", e.toString());
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
    }
}
