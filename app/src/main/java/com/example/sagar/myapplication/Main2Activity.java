package com.example.sagar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
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
        Intent showFuelActivity = new Intent (this, SpeedRPMActivity.class);
        startActivity(showFuelActivity);
    }

    public void showThrottlePosition (View view) {
        Intent showThrottlePosition = new Intent (this, ThrottlePosition.class);
        startActivity(showThrottlePosition);
    }

    public void showRpmActivity(View view) {
        Intent showRPMActivity = new Intent (this, RpmActivity.class);
        startActivity(showRPMActivity);
    }

    public void showTempActivity(View view) {
        Intent showTempActivity = new Intent (this, TemperatureActivity.class);
        startActivity(showTempActivity);
    }

    public void showFuelPressure (View view) {
        Intent showFuelPressure = new Intent (this, FuelPressure.class);
        startActivity(showFuelPressure);
    }

    public void showMAF (View view) {
        Intent showMAF = new Intent (this, MassAirFlow.class);
        startActivity(showMAF);
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
    public void onDestroy() {
        super.onDestroy();
    }
}
