package com.example.sagar.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sagar.myapplication.model.Drive;
import com.example.sagar.myapplication.model.ProcessedMessage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by achal on 2018-03-07.
 */

public class HomeFragment extends Fragment {
    private BluetoothAdapter bluetoothAdapter;
    private static final String TAG = "MY_APP_DEBUG_TAG";

    private BluetoothThread thread;
    private BluetoothDevice bluetoothDevice;

    private Drive previousDrive;
    private List<ProcessedMessage> previousMessages;

    //    private final String BLUETOOTH_DEVICE = "sagarpi";
    private final String BLUETOOTH_DEVICE = "DESKTOP-46PD4HS";

    private boolean isDriving = false;
    private Button btn;
    private Chronometer clock;
    private TextView driveListTitle;
    private ScrollView scrollView;
    private TextView fuelSystemStatus, fuelEconomy, dateTitle;

    private LineChart engineLoadChart;
    private LineDataSet lineDataSet;
    private LineData lineData;
    private View spacer;

    public CardView speed, throttlePosition, rpm, coolantTemp, fuelPressure, maf;

    private ArrayList<Entry> previousValues = new ArrayList<>();

    private Thread pollingThread;

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
        dateTitle = view.findViewById(R.id.txtDriveHomeDate);
        spacer = view.findViewById(R.id.spacer);

        //Feature Cards
        speed = view.findViewById(R.id.speedCard);
        throttlePosition = view.findViewById(R.id.throttlePositionCard);
        rpm = view.findViewById(R.id.rpmCard);
        coolantTemp = view.findViewById(R.id.coolantTempCard);
        fuelPressure = view.findViewById(R.id.fuelPressureCard);
        maf = view.findViewById(R.id.mafCard);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) { return view; }

        initializeChart();
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
                    spacer.setVisibility(View.VISIBLE);
                    startDrive();

                } else {
                    isDriving = false;
                    btn.setText("Start Drive");
                    driveListTitle.setText("Recent Drive");
                    clock.setVisibility(view.GONE);
                    changeScrollViewWeight(80);
                    spacer.setVisibility(View.INVISIBLE);
                    stopDrive();
                }
            }
        });
    }

    private void initializeChart() {
        engineLoadChart.setPinchZoom(false);
        engineLoadChart.setDragEnabled(false);
        engineLoadChart.setScaleEnabled(false);

        Description description = new Description();
        description.setText("");
        engineLoadChart.setDescription(description);
        engineLoadChart.getLegend().setEnabled(false);

        lineDataSet = new LineDataSet(new ArrayList(Arrays.asList(new Entry(0,0))), "");
        lineDataSet.setLineWidth(3);
        lineDataSet.setValueTextSize(0);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setCircleRadius(4);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawCircles(false);

        lineData = new LineData(lineDataSet);
        engineLoadChart.setData(lineData);
    }

    public void changeScrollViewWeight (int val) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scrollView
                .getLayoutParams();
        layoutParams.weight = val;
        scrollView.requestLayout();
    }

    private void enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            queryPreviousDrive();
            findBluetoothDevice();
        }
    }

    private void startDrive() {
        thread = new BluetoothThread(bluetoothDevice);
        new Thread(thread).start();
        startClock();
        dateTitle.setVisibility(View.INVISIBLE);
        pollCurrentData();
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

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        System.out.println("Activity Result" + reqCode);
        if (resultCode == RESULT_CANCELED) {
            System.out.println("Bluetooth not enabled.");
            queryPreviousDrive();
        } else {
            System.out.println("Bluetooth enabled!");
            queryPreviousDrive();
            findBluetoothDevice();
        }
    }

    public void findBluetoothDevice() {
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

    private void queryPreviousDrive() {
        Call<Drive> call = RestHelper.getLastDrive();
        call.enqueue(new Callback<Drive>() {
            @Override
            public void onResponse(Call<Drive> call, Response<Drive> response) {
                previousDrive = response.body();
                loadPreviousData();
            }

            @Override
            public void onFailure(Call<Drive> call, Throwable t) {
                Log.e(TAG, "Couldn't retrieve previous drive", t);
            }
        });
    }

    private void loadPreviousData() {
        Call<List<ProcessedMessage>> call = RestHelper.getDataByDrive(previousDrive.getId());
        call.enqueue(new Callback<List<ProcessedMessage>>() {
            @Override
            public void onResponse(Call<List<ProcessedMessage>> call, Response<List<ProcessedMessage>> response) {
                Log.d(TAG, "Getting data for: " + previousDrive.getId());
                previousMessages = response.body();
                Log.d(TAG, "Got previous data: " + previousMessages.size() + " messages");
                populatePreviousData();
            }

            @Override
            public void onFailure(Call<List<ProcessedMessage>> call, Throwable t) {
                Log.e(TAG, "Couldn't retrieve previous drive", t);
            }
        });
    }

    private void populatePreviousData() {
        if(previousMessages.size() == 0) return;
        populateEngineLoadChartWithPreviousData();
        redrawChart();
        Object fuelStatus = previousMessages.get(0).getValues().get("FUEL_SYSTEM_STATUS");
        setFuelSystemStatus(fuelStatus);

        setFuelEconomyAverage();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy, h:mm a");
        String dateString = simpleDateFormat.format(previousDrive.getStart());
        dateTitle.setText(dateString);
        dateTitle.setVisibility(View.VISIBLE);
    }

    public void setFuelSystemStatus(Object status) {
        if (status == null) {
            fuelSystemStatus.setText("Good");
        } else {
            fuelSystemStatus.setText(status.toString());
        }
    }

    public void populateEngineLoadChartWithPreviousData() {
        if(previousMessages.size() == 0) {
            return;
        }

        engineLoadChart.setVisibleXRangeMaximum(previousMessages.size());
        lineDataSet.clear();
        for(int i = 0; i < previousMessages.size(); i++) {
            ProcessedMessage message = previousMessages.get(i);
            Double val = (Double) message.getValues().get("THROTTLE_POSITION");

            lineDataSet.addEntry(new Entry(i,val.floatValue()));
        }
        redrawChart();
    }

    private void redrawChart() {
        if(lineData == null) {
            return;
        }

        engineLoadChart.post(() -> {
            lineData.notifyDataChanged();
            engineLoadChart.notifyDataSetChanged();
            engineLoadChart.invalidate();
        });
    }

    private void pollCurrentData() {
        engineLoadChart.setVisibleXRangeMaximum(100);
        lineDataSet.clear();
        pollingThread = new Thread(() -> {
            System.out.println("Started polling thread.");
            while (isDriving) {
                Call<ProcessedMessage> call = RestHelper.getLatestMessage();
                call.enqueue(new Callback<ProcessedMessage>() {

                    @Override
                    public void onResponse(Call<ProcessedMessage> call, Response<ProcessedMessage> response) {
                        ProcessedMessage message = response.body();
                        Double val = (Double) message.getValues().get("THROTTLE_POSITION");
                        lineDataSet.addEntry(new Entry(lineDataSet.getValues().size(), val.floatValue()));
                        redrawChart();

                        Object econValue = message.getValues().get("FUEL_ECONOMY");
                        setFuelEconomy(econValue);

                        setFuelSystemStatus(message.getValues().get("FUEL_SYSTEM_STATUS"));
                    }

                    @Override
                    public void onFailure(Call<ProcessedMessage> call, Throwable t) {}
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            queryPreviousDrive();
        });

        pollingThread.start();
    }

    private void setFuelEconomyAverage() {
        double total = 0;
        double average;
        for (ProcessedMessage p : previousMessages) {
            total += getFuelEconValue(p.getValues().get("FUEL_ECONOMY"));
        }

        average = total / previousMessages.size();

        DecimalFormat df = new DecimalFormat("#.##");
        String text = "Average: " + df.format(average) + "L/100KM";
        fuelEconomy.setText(text);
    }

    private void setFuelEconomy(Object econValue) {
        DecimalFormat df = new DecimalFormat("#.##");
        Double val = getFuelEconValue(econValue);
        String text = df.format(val) + "L/100KM";
        fuelEconomy.setText(text);
    }

    private double getFuelEconValue(Object econValue) {
        if (econValue == null || econValue.toString().equals("Infinity")) {
            return 12.034;
        } else {
            double fuelEcon = (double) econValue;
            return fuelEcon;
        }
    }
}
