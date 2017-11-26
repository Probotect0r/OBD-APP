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
    // The UUID of the SerialPort bluetooth service
    UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private static final String TAG = "MY_APP_DEBUG_TAG";
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;

    private InputStream inputStream;
    private OutputStream outputStream;

    // mmBuffer store for the stream
    private byte[] mmBuffer;

    private boolean continuePolling = true;

    String command = "010c\r";
    public BluetoothThread(BluetoothDevice device) {
        this.bluetoothDevice = device;

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
            Log.d(TAG, "Connected.");
        } catch (IOException err) {
            Log.e(TAG, "Error connecting to device", err);
            return;
        }

        // Start polling the device
        while(this.continuePolling) {
            // Write the command
            try {
                this.write(this.command.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error writing data: ", e);
                break;
            }

            String message = null;
            try {
                message = this.getString();
            } catch (IOException e) {
                Log.e("Error rawdata: ", e.toString());
                break;
            }

            Log.d(TAG, message);

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
}
