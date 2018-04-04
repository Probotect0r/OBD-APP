package com.example.sagar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class Main2Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment());

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
            case R.id.navigation_notifications:
                fragment = new HistoricalFragment();
                break;
        }

        return loadFragment(fragment);
    }

    //UI Activities

    public void showSpeedActivity(View view) {
        Intent showFuelActivity = new Intent (this, LineChartActivity.class);
        showFuelActivity.putExtra("key", "SPEED");
        showFuelActivity.putExtra("title", "Speed");
        showFuelActivity.putExtra("xAxis", "Seconds");
        showFuelActivity.putExtra("yAxis", "KM/H");
        startActivity(showFuelActivity);
    }

    public void showThrottlePosition (View view) {
        Intent showThrottlePosition = new Intent (this, LineChartActivity.class);
        showThrottlePosition.putExtra("key", "THROTTLE_POSITION");
        showThrottlePosition.putExtra("title", "Throttle Position");
        showThrottlePosition.putExtra("xAxis", "Seconds");
        showThrottlePosition.putExtra("yAxis", "%");
        startActivity(showThrottlePosition);
    }

    public void showRpmActivity(View view) {
        Intent showRPMActivity = new Intent (this, LineChartActivity.class);
        showRPMActivity.putExtra("key", "RPM");
        showRPMActivity.putExtra("title", "RPM");
        showRPMActivity.putExtra("xAxis", "Seconds");
        showRPMActivity.putExtra("yAxis", "RPM");
        startActivity(showRPMActivity);
    }

    public void showTempActivity(View view) {
        Intent showTempActivity = new Intent (this, LineChartActivity.class);
        showTempActivity.putExtra("key", "COOLANT_TEMPERATURE");
        showTempActivity.putExtra("title", "Coolant Temprature");
        showTempActivity.putExtra("xAxis", "Seconds");
        showTempActivity.putExtra("yAxis", "50&#x2103;C");
        startActivity(showTempActivity);
    }


    public void showFuelPressure (View view) {
        Intent showFuelPressure = new Intent (this, LineChartActivity.class);
        showFuelPressure.putExtra("key", "FUEL_PRESSURE");
        showFuelPressure.putExtra("title", "Fuel Pressure");
        showFuelPressure.putExtra("xAxis", "Seconds");
        showFuelPressure.putExtra("yAxis", "KPa");
        startActivity(showFuelPressure);
    }

    public void showMAF (View view) {
        Intent showMAF = new Intent (this, LineChartActivity.class);
        showMAF.putExtra("key", "MAF");
        showMAF.putExtra("title", "Mass Air Flow");
        showMAF.putExtra("xAxis", "Seconds");
        showMAF.putExtra("yAxis", "g/s");
        startActivity(showMAF);
    }

    public void showErrors(View view) {
        Intent showErrors = new Intent(this, ViewErrorCodes.class);
        startActivity(showErrors);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
