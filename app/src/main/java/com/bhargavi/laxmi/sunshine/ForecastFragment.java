package com.bhargavi.laxmi.sunshine;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bhargavi.laxmi.sunshine.service.ServiceManager;
import com.bhargavi.laxmi.sunshine.service.SunshineService;
import com.bhargavi.laxmi.sunshine.service.data.WeatherResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    public static final String EXTRA_FORECAST_ARRAY = "forecastArray";

    public static final String EXTRA_POSITION = "listViewPosition";

    private WeatherAdapter weatherAdapter;
    private ListView weatherListView;
    private String userLocation;
    private String unit;
    private boolean isTablet;
    private WeatherResponse.WeatherData[] mWeatherDataArray;
    private int mSelectedPosition;

    private ForceFragmentInterface mListener;

    private SharedPreferences sharedPref;

    private DataReceiver receiver;

    public interface ForceFragmentInterface {
        void onListItemClicked(WeatherResponse.WeatherData weatherData);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (ForceFragmentInterface) activity;
    }

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        unit = sharedPref.getString(getString(R.string.units_pref), "C");

        userLocation = sharedPref.getString(getString(R.string.location_pref_key), "95051");
        isTablet = getResources().getBoolean(R.bool.tablet);


        weatherListView = (ListView) view.findViewById(R.id.listview_forecast);
        weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherResponse.WeatherData weatherData = (WeatherResponse.WeatherData) weatherAdapter.getItem(position);
                mListener.onListItemClicked(weatherData);
                mSelectedPosition = position;
            }
        });

        if (savedInstanceState != null) {
            mWeatherDataArray = (WeatherResponse.WeatherData[]) savedInstanceState.getParcelableArray(EXTRA_FORECAST_ARRAY);
            mSelectedPosition = savedInstanceState.getInt(EXTRA_POSITION);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(EXTRA_FORECAST_ARRAY, mWeatherDataArray);
        outState.putInt(EXTRA_POSITION, mSelectedPosition);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStart() {
        super.onStart();

        String location = sharedPref.getString(getString(R.string.location_pref_key), "95051");
        if (mWeatherDataArray == null || !userLocation.equals(location)) {

            if (receiver == null){
                receiver = new DataReceiver();
            }

            IntentFilter filter = new IntentFilter(SunshineService.ACTION_DATA_LOADED);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,filter);
            userLocation = location;
            updateWeather();
        } else {
            finishLoading();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(receiver!= null){
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        }
    }

    private void updateWeather() {
        Intent intent = new Intent(getActivity(), SunshineService.class);
        intent.putExtra(SunshineService.EXTRA_USER_LOCATION,userLocation);
        getActivity().startService(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        } else if (id == R.id.action_settings) {

            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);

            return true;

        } else if (id == R.id.action_map) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + userLocation);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            if (isPackageInstalled("com.google.android.apps.maps", getActivity())) {

                mapIntent.setPackage("com.google.android.apps.maps");

            }
            startActivity(mapIntent);
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



    private void finishLoading() {
        weatherAdapter = new WeatherAdapter(getActivity(), mWeatherDataArray, unit, isTablet);
        weatherListView.setAdapter(weatherAdapter);
        weatherListView.setItemChecked(mSelectedPosition, true);

    }

    private class DataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if ( intent.getAction().equals(SunshineService.ACTION_DATA_LOADED) && isAdded()) {
                ArrayList<WeatherResponse.WeatherData> arrayList = intent.getParcelableArrayListExtra(EXTRA_FORECAST_ARRAY);
                mWeatherDataArray =  arrayList.toArray(new WeatherResponse.WeatherData[arrayList.size()]);
                if (mWeatherDataArray.length > 0 && isTablet) {
                    mListener.onListItemClicked(mWeatherDataArray[0]);
                }
                finishLoading();
            }
        }
    }
}

/**/