package com.bhargavi.laxmi.sunshine.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.bhargavi.laxmi.sunshine.ForecastFragment;
import com.bhargavi.laxmi.sunshine.service.data.WeatherResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by laxmi on 7/6/15.
 */
public class SunshineService extends IntentService {

    public static final String EXTRA_USER_LOCATION = "userLocation";

    public static final String ACTION_DATA_LOADED = "dataLoaded";

    public SunshineService() {
        super("Sunshine Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            String userLocation = intent.getStringExtra(EXTRA_USER_LOCATION);

            WeatherResponse weatherResponse = ServiceManager.getWeatherResponse(userLocation);
            WeatherResponse.WeatherData[] weatherDataList = weatherResponse.getList();

            ArrayList<WeatherResponse.WeatherData> arrayList = new ArrayList<WeatherResponse.WeatherData>(Arrays.asList(weatherDataList));


            Intent resIntent = new Intent(ACTION_DATA_LOADED);
            resIntent.putParcelableArrayListExtra(ForecastFragment.EXTRA_FORECAST_ARRAY,arrayList);
            LocalBroadcastManager.getInstance(this).sendBroadcast(resIntent);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
