package com.example.sagar.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class speedRpm extends AppCompatActivity {
    TextView speed, rpm;
    double kmh = 105, revloutions = 3000;

    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_rpm);

        speed = findViewById(R.id.speed);
        rpm = findViewById(R.id.rpm);

        speed.setText("Speed: " + kmh);
        rpm.setText("RPM: " + revloutions);
    }
    public void backToHome(View view) {
        finish();
    }
}
