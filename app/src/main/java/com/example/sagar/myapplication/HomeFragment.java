package com.example.sagar.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Set;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by achal on 2018-03-07.
 */

public class HomeFragment extends Fragment {
    private BluetoothAdapter bluetoothAdapter;
    private UUID uuid = UUID.randomUUID();
    private static final String TAG = "MY_APP_DEBUG_TAG";

    private BluetoothThread thread;
    private BluetoothDevice bluetoothDevice;

    //    private final String BLUETOOTH_DEVICE = "sagarpi";
    private final String BLUETOOTH_DEVICE = "DESKTOP-46PD4HS";

    private boolean isDriving = false;
    private Button btn;
    private Chronometer clock;
    private TextView driveListTitle;
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        btn = view.findViewById(R.id.startStop);
        clock = view.findViewById(R.id.chronometerHome);
        driveListTitle = view.findViewById(R.id.txtDriveHome);
        scrollView = view.findViewById(R.id.scrollHome);

        setupButtonListener();
        enableBluetooth();
        return view;
    }

    private void setupButtonListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isDriving) {
                    isDriving = true;
                    btn.setText("Stop Drive");
                    driveListTitle.setText("Current Drive");
                    changeScrollViewWeight(70);
                    clock.setVisibility(view.VISIBLE);
                    startDrive();

                } else {
                    isDriving = false;
                    btn.setText("Start Drive");
                    driveListTitle.setText("Recent Drive");
                    clock.setVisibility(view.GONE);
                    changeScrollViewWeight(80);
                    stopDrive();
                }
            }
        });
    }

    private void enableBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            setupBluetoothConnection();
        }
    }

    private void startDrive() {
        thread = new BluetoothThread(bluetoothDevice);
        new Thread(thread).start();
        startClock();
    }

    private void stopDrive() {
        thread.setContinuePolling(false);
        stopClock();
    }

    public void startClock () {
        clock.setBase(SystemClock.elapsedRealtime());
        clock.start();
    }

    public void stopClock () {
        clock.stop();
        clock.setBase(SystemClock.elapsedRealtime());
        long elapsedMills = SystemClock.elapsedRealtime() - clock.getBase();
    }

    public void changeScrollViewWeight (int val) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scrollView
                .getLayoutParams();
        layoutParams.weight = val;
        scrollView.requestLayout();
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
                    Log.d(TAG, "Found bluetooth device.");
                    bluetoothDevice = device;
                    break;
                }
            }
        }
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
}
