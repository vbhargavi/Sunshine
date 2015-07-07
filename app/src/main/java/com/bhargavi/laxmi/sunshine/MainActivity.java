package com.bhargavi.laxmi.sunshine;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bhargavi.laxmi.sunshine.service.data.WeatherResponse;


public class MainActivity extends AppCompatActivity implements ForecastFragment.ForceFragmentInterface {

    private boolean isLandscape;
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLandscape = getResources().getBoolean(R.bool.landscape);
        isTablet = getResources().getBoolean(R.bool.tablet);
    }


    @Override
    public void onListItemClicked(WeatherResponse.WeatherData weatherData) {
        if (isTablet) {
            showDetailsFragment(weatherData);
        } else {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("forecast", weatherData);
            startActivity(intent);
        }
    }

    private void showDetailsFragment(WeatherResponse.WeatherData weatherData) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailActivityFragment fragment;
        if (getFragmentManager().findFragmentByTag("DetailsFragment") != null) {
            fragment = (DetailActivityFragment) getFragmentManager().findFragmentByTag("DetailsFragment");
            fragment.updateData(weatherData);
        } else {
            fragment = DetailActivityFragment.newInstance(weatherData);
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment, "DetailsFragment");
        fragmentTransaction.commit();
    }
}
