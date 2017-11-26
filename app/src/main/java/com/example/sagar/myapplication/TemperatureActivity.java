package com.example.sagar.myapplication;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TemperatureActivity extends AppCompatActivity {

    ImageView img;
    TextView value;

    //Temporary change to any value later
    private int temp = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temprature);

        img = findViewById(R.id.thermometer);


        //change color of the SVG depending on the temprature being too hot, too, cold or just right
        if (temp > 100) {
            img.setColorFilter(ContextCompat.getColor(this, R.color.veryRed), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (temp < 50) {
            img.setColorFilter(ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            img.setColorFilter(ContextCompat.getColor(this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
        }


        value = findViewById(R.id.txtEngineTemp);

        value.setText("Engine Temp: " + Integer.toString(temp));
    }

    //Return back to home screen
    public void backToHome(View view) {
        finish();
    }
}

