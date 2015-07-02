package com.bhargavi.laxmi.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhargavi.laxmi.sunshine.service.data.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private ShareActionProvider shareActionProvider ;
    private WeatherResponse.WeatherData mWeatherData;

    TextView dayTextView;
    TextView dateTextView;
    TextView maxTempTextView;
    TextView minTempTextView;
    TextView humidityTextView;
    TextView windTextView;
    TextView pressureTextView;
    TextView weatherTextView;
    ImageView weatherImageView;



    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
       //TextView textView = (TextView) view.findViewById(R.id.detail_text_view);

        dayTextView = (TextView) view.findViewById(R.id.detail_text_view);
        dateTextView = (TextView) view.findViewById(R.id.date_text_view);
        maxTempTextView = (TextView) view.findViewById(R.id.maxtemp_text_view);
        minTempTextView = (TextView) view.findViewById(R.id.mintemp_text_view);
        humidityTextView = (TextView) view.findViewById(R.id.humidity_text_view);
        windTextView = (TextView) view.findViewById(R.id.wind_text_view);
        pressureTextView = (TextView) view.findViewById(R.id.pressure_text_view);
        weatherImageView = (ImageView) view.findViewById(R.id.weatherimage_view);
        weatherTextView = (TextView) view.findViewById(R.id.weathertype_text_view);

        Bundle bundle = getActivity().getIntent().getExtras();

         mWeatherData = bundle.getParcelable("forecast");
        initData();
        //textView.setText(forecast);
        return view;
    }

    private void initData(){
        int humidity = mWeatherData.getHumidity();
        double pressure = mWeatherData.getPressure();
        double speed = mWeatherData.getSpeed();
        long dt = mWeatherData.getDt();
        WeatherResponse.WeatherData.Temperature temp = mWeatherData.getTemp();
        double max = temp.getMax();
        double min = temp.getMin();
        WeatherResponse.WeatherData.Weather weather = mWeatherData.getWeather();
        String main = weather.getMain();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isMetric = sharedPref.getString(getString(R.string.units_pref), "C").equals("C");

        String maxTempString = Utility.formatTemperature(getActivity(), max, isMetric);
        String minTempString = Utility.formatTemperature(getActivity(), min, isMetric);
        long milliSeconds = mWeatherData.getDt()*1000;

        String day = Utility.getFriendlyDayString(getActivity(), milliSeconds);
        Date date = new Date(milliSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d", Locale.US);



        humidityTextView.setText("Humidity: " + humidity );
        pressureTextView.setText("Pressure: " + pressure);;
        windTextView.setText("Wind: " + speed);
        weatherTextView.setText(main);
        dayTextView.setText(day);
        maxTempTextView.setText(maxTempString);
        minTempTextView.setText(minTempString);
        dateTextView.setText(sdf.format(date));







    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);


        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //shareIntent.putExtra(Intent.EXTRA_TEXT, forecast);
        setShareIntent(shareIntent);


    }

    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
