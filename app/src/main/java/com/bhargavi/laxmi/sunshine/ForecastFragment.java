package com.bhargavi.laxmi.sunshine;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private ArrayAdapter<String> weatherAdapter ;
    private ListView weatherListView ;

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
       /* ArrayList<String> weather = new ArrayList<String>();
        weather.add("Today - sunny - 88/63");
        weather.add("Tomorrow -cloudy- 60/74");
        weather.add("Wednesday -foggy- 74/63");
        weather.add("Thursday -sunny- 88/63");
        weather.add("Friday -sunny- 88/63");
        weather.add("Saturday -cloudy- 70/66");
        weather.add("Sunday -cloudy- 74/63");
        weather.add("Monday -sunny- 88/63");*/

         weatherListView = (ListView) view.findViewById(R.id.listview_forecast);

        FetchWeatherTask task = new FetchWeatherTask();
        task.execute();

        return view;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchWeatherTask extends AsyncTask<Void,Void,ArrayList<String>> {

         @Override
         protected ArrayList<String> doInBackground(Void... params) {
             try {
                 WeatherResponse weatherResponse = ServiceManager.getWeatherResponse("95051");
                 ArrayList<String> response = new ArrayList<String>();
                 WeatherResponse.WeatherData[] weatherDataList = weatherResponse.getList();
                 for (WeatherResponse.WeatherData data : weatherDataList)
                 {
                     Date dt = new Date(data.getDt() * 1000);

                     SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d");


                     WeatherResponse.WeatherData.Weather weather =  data.getWeather()[0];
                     String weatherInfo = weather.getMain();

                     WeatherResponse.WeatherData.Temperature temp = data.getTemp();

                     String result = sdf.format(dt) + " - " + weatherInfo + " - " + temp.getMax() + "/" + temp.getMin();
                     response.add(result);
                 }

                 return response;
             } catch (IOException e) {
                 e.printStackTrace();
             }
             

             return null;
         }

         @Override
         protected void onPostExecute(ArrayList<String> weatherResponse) {
             super.onPostExecute(weatherResponse);
             Log.d(getClass().getSimpleName(), weatherResponse.toString());

             weatherAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,weatherResponse);

             weatherListView.setAdapter(weatherAdapter);

         }
     }
}
