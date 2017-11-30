package com.example.sagar.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sagar.myapplication.RPM.RpmActivity;
import com.example.sagar.myapplication.RPM.SpeedRPMActivity;

import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private UUID uuid = UUID.randomUUID();
    private static final String TAG = "MY_APP_DEBUG_TAG";

    private BluetoothThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Check if bluetooth is supported, if not, dont start bluetooth thread
        if (this.bluetoothAdapter == null) {
            return;
        }

        // Check if bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            // Not enabled, request access
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            // Bluetooth already enabled
            this.setupBluetoothConnection();

        }

    }

    //UI Activities
    public void showFuelActivity(View view) {
        Intent showFuelActivity = new Intent (this, RpmActivity.class);
        startActivity(showFuelActivity);
    }

    public void showTempActivity(View view) {
        Intent showFuelActivity = new Intent (this, TemperatureActivity.class);
        startActivity(showFuelActivity);
    }

    public void showSpeedActivity(View view) {
        Intent showFuelActivity = new Intent (this, SpeedRPMActivity.class);
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
            this.setupBluetoothConnection();
        }
    }

    public void setupBluetoothConnection() {
        Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();

                if (deviceName.equals("DESKTOP-46PD4HS")) {
                    // Connect to this device
                    this.thread = new BluetoothThread(device);
                    this.thread.start();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.thread.setContinuePolling(false);
    }
}

