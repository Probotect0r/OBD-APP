package com.example.sagar.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sagar.myapplication.RPM.RpmActivity;
import com.example.sagar.myapplication.RPM.SpeedRPMActivity;

import java.util.Set;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BluetoothAdapter bluetoothAdapter;
    private UUID uuid = UUID.randomUUID();
    private static final String TAG = "MY_APP_DEBUG_TAG";

    private BluetoothThread thread;
//    private final String BLUETOOTH_DEVICE = "sagarpi";
    private final String BLUETOOTH_DEVICE = "DESKTOP-46PD4HS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment());

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.bluetoothAdapter == null) {
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            this.setupBluetoothConnection();

        }
    }

    private boolean loadFragment (Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.fade_out)
                    .replace(R.id.fragment_content, fragment)
                    .commit();

            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch(item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_dashboard:
                fragment = new Dash_fragment();
                break;
            case R.id.navigation_notifications:
                fragment = new HistoricalFragment();
                break;
        }

        return loadFragment(fragment);
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
        Log.d(TAG, "Looking for bluetooth device.");
        Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            Log.d(TAG, "Found list of paired devices.");

            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                Log.d(TAG, "Device: " + deviceName);

                if (deviceName.equals(BLUETOOTH_DEVICE)) {
                    Log.d(TAG, "Found bluetooth device, starting bluetooth thread.");
                    thread = new BluetoothThread(device);
                    new Thread(thread).start();
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
