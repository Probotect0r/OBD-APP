package com.example.sagar.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private UUID uuid = UUID.randomUUID();
    private static final String TAG = "MY_APP_DEBUG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Set up bluetooth
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            // Not enabled, request access
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            // Bluetooth already enabled
            System.out.println("Bluetooth already enabled. Querying paired devices.");

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();
                    System.out.println(deviceName);
                    System.out.println(deviceAddress);

                    if (deviceName.equals("DESKTOP-46PD4HS")) {
                        // Connect to this device
                        System.out.println("Connecting to " + deviceName);
                        try {
                            BluetoothSocket socket  = device.createRfcommSocketToServiceRecord(this.uuid);
                            ConnectThread thread = new ConnectThread(device);
                            thread.start();
                        } catch (IOException err) {
                            System.out.println(err);
                        }
                    }
                }
            }
        }

    }

    //UI Activities
    public void showFuelActivity(View view) {
        Intent showFuelActivity = new Intent (this, fuel.class);
        startActivity(showFuelActivity);
    }

    public void showTempActivity(View view) {
        Intent showFuelActivity = new Intent (this, temprature.class);
        startActivity(showFuelActivity);
    }

    public void showSpeedActivity(View view) {
        Intent showFuelActivity = new Intent (this, speedRpm.class);
        startActivity(showFuelActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        System.out.println("Activity Result" + reqCode);
        if (resultCode == RESULT_CANCELED) {
            System.out.println("Bluetooth not enabled.");
        } else {
            System.out.println("Bluetooth enabled!");
        }
    }

}

class ConnectThread extends Thread {
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // bluetooth serial port service

    public ConnectThread(BluetoothDevice device) {
        this.bluetoothDevice = device;
        try {

            this.bluetoothSocket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
        } catch (IOException err) {
            Log.e(TAG, "Error creating socket", err);
        }
    }

    public void run() {
        try {
            this.bluetoothSocket.connect();
            System.out.println("CONNECTED!");
            CommunicationThread communicationThread =  new CommunicationThread(this.bluetoothSocket);
            communicationThread.start();

            String strMessage = "ATZ\r";
            communicationThread.write(strMessage.getBytes());

        } catch (IOException err) {
            Log.e(TAG, "Error connecting to device", err);
            try {

                this.bluetoothSocket =(BluetoothSocket) this.bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(this.bluetoothDevice,1);
                this.bluetoothSocket.connect();
            } catch(Exception e) {
                Log.e(TAG, "Error connecting with reflection", e);
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
}

 class CommunicationThread extends Thread {
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private BluetoothSocket bluetoothSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer; // mmBuffer store for the stream

    public CommunicationThread(BluetoothSocket socket) {
        this.bluetoothSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

    }

    public void run() {
        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            String response = getString();
            Log.d(TAG, "Got message: " + response);
        }

    }

    public String getString() {
        StringBuilder res = new StringBuilder();

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                byte b = (byte) mmInStream.read();

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
            mmOutStream.write(bytes);
            mmOutStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
    }
}
