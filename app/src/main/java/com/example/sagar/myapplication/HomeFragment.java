package com.example.sagar.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import com.example.sagar.myapplication.model.Drive;
import com.example.sagar.myapplication.model.ProcessedMessage;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by achal on 2018-03-07.
 */

public class HomeFragment extends Fragment {
    private BluetoothAdapter bluetoothAdapter;
    private UUID uuid = UUID.randomUUID();
    private static final String TAG = "MY_APP_DEBUG_TAG";

    private Retrofit retrofit;
    private RetrieveService retrieveService;

    private BluetoothThread thread;
    private BluetoothDevice bluetoothDevice;

    private Drive drive;
    private List<ProcessedMessage> messages;

    //    private final String BLUETOOTH_DEVICE = "sagarpi";
    private final String BLUETOOTH_DEVICE = "DESKTOP-46PD4HS";

    private boolean isDriving = false;
    private Button btn;
    private Chronometer clock;
    private TextView driveListTitle;
    private ScrollView scrollView;
    private TextView fuelSystemStatus, fuelEconomy;
    private LineChart engineLoadChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        btn = view.findViewById(R.id.startStop);
        clock = view.findViewById(R.id.chronometerHome);
        driveListTitle = view.findViewById(R.id.txtDriveHome);
        scrollView = view.findViewById(R.id.scrollHome);
        fuelSystemStatus = view.findViewById(R.id.txtFuelSystemValue);
        fuelEconomy = view.findViewById(R.id.txtFuelEconomy);
        engineLoadChart = view.findViewById(R.id.homeEngineLoadChart);

        if (bluetoothAdapter == null) { return view; }

        setupButtonListener();
        setupRetrofit();
        enableBluetooth();
        engineLoadChartData();
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

    private void setupRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://" + BluetoothThread.API_ADDRESS + ":8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.retrieveService = retrofit.create(RetrieveService.class);
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

    //Change Fuel System Status
    public void changeFuelSystemStatusValue(String status) { fuelSystemStatus.setText(status); }

    //Change Fuel Economy Value
    public void changeFuelEconomyValue (int val) { fuelEconomy.setText(val + "L/100 KM"); }

    //populate Line chart for Engine Load
    public void engineLoadChartData () {

        engineLoadChart.setPinchZoom(false);
        engineLoadChart.setDragEnabled(false);
        engineLoadChart.setScaleEnabled(false);

        ArrayList <Entry> yValues = new ArrayList<>();

        yValues.add(new Entry (0 ,1 ));
        yValues.add(new Entry (1 ,3 ));
        yValues.add(new Entry (2 ,5 ));


        LineDataSet lineDataSet = new LineDataSet(yValues, "Data Set 1");
        lineDataSet.setLineWidth(3);
        lineDataSet.setValueTextSize(0);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setCircleRadius(4);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawCircles(false);


        ArrayList <ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);

        engineLoadChart.setData(lineData);

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

    private void queryPreviousDrive() {
        Call<Drive> call = this.retrieveService.getLastDrive();
        call.enqueue(new Callback<Drive>() {
            @Override
            public void onResponse(Call<Drive> call, Response<Drive> response) {
                drive = response.body();
            }

            @Override
            public void onFailure(Call<Drive> call, Throwable t) {
                Log.e(TAG, "Couldn't retrieve previous drive", t);
            }
        });
    }

    private void getPreviousDriveData() {
        Call<List<ProcessedMessage>> call = this.retrieveService.getData(drive.getId());
        call.enqueue(new Callback<List<ProcessedMessage>>() {
            @Override
            public void onResponse(Call<List<ProcessedMessage>> call, Response<List<ProcessedMessage>> response) {
                messages = response.body();
            }

            @Override
            public void onFailure(Call<List<ProcessedMessage>> call, Throwable t) {
                Log.e(TAG, "Couldn't retrieve previous drive", t);
            }
        });
    }
}
