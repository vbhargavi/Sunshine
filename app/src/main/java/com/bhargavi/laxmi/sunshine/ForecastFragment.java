package com.bhargavi.laxmi.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
    private WeatherAdapter weatherAdapter ;
    private ListView weatherListView ;
    private String userLocation;
    private  String unit ;


    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        unit = sharedPref.getString(getString(R.string.units_pref), "C");

        userLocation = sharedPref.getString(getString(R.string.location_pref_key), "95051");



        weatherListView = (ListView) view.findViewById(R.id.listview_forecast);
        weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*String forecast = weatherAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("forecast", forecast);
                startActivity(intent);*/
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    private void updateWeather(){
        FetchWeatherTask task = new FetchWeatherTask();
        task.execute();
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
        }
        else if (id == R.id.action_settings){

            Intent intent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);

            return true;

        }
        else if (id == R.id.action_map){
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + userLocation);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            if (isPackageInstalled("com.google.android.apps.maps",getActivity())) {

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

    private class FetchWeatherTask extends AsyncTask<Void,Void,WeatherResponse.WeatherData[]> {

         @Override
         protected WeatherResponse.WeatherData[] doInBackground(Void... params) {

             try {

                 WeatherResponse weatherResponse = ServiceManager.getWeatherResponse(userLocation);
                 WeatherResponse.WeatherData[] weatherDataList = weatherResponse.getList();
                /* for (WeatherResponse.WeatherData data : weatherDataList)
                 {
                     Date dt = new Date(data.getDt() * 1000);

                     SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d");


                     WeatherResponse.WeatherData.Weather weather =  data.getWeather()[0];
                     String weatherInfo = weather.getMain();

                     WeatherResponse.WeatherData.Temperature temp = data.getTemp();

                     double max = temp.getMax();
                     double min =  temp.getMin();

                     if (unit.equals("F")){
                         max = max * (9/5) + 32;
                         min = min * (9/5) + 32;

                     }

                     String result = sdf.format(dt) + " - " + weatherInfo + " - " + max + "/" + min ;
                     response.add(result);
                 }*/

                 return weatherDataList;
             } catch (IOException e) {
                 e.printStackTrace();
             }
             

             return null;
         }

         @Override
         protected void onPostExecute(WeatherResponse.WeatherData[] weatherResponse) {
             super.onPostExecute(weatherResponse);
             Log.d(getClass().getSimpleName(), weatherResponse.toString());

             weatherAdapter = new WeatherAdapter(getActivity(),weatherResponse,unit);
             weatherListView.setAdapter(weatherAdapter);

         }
     }
}
