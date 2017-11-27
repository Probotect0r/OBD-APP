package com.example.sagar.myapplication.RPM;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.sagar.myapplication.R;

import retrofit2.Retrofit;

public class SpeedRPMActivity extends AppCompatActivity {
    TextView speed, rpm;
    double kmh = 105, revloutions = 3000;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .build();

    RPMService service = retrofit.create(RPMService.class);
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
