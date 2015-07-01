package com.bhargavi.laxmi.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhargavi.laxmi.sunshine.service.data.WeatherResponse;

/**
 * Created by laxmi on 6/30/15.
 */
public class WeatherAdapter extends BaseAdapter {

    private WeatherResponse.WeatherData[] mWeatherData;
    private Context mContext;
    private LayoutInflater mInflater;
    private  String mUnit;

    public WeatherAdapter(Context context, WeatherResponse.WeatherData[] weatherData,String unit) {
        mWeatherData = weatherData;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mUnit = unit;
    }


    @Override
    public int getCount() {
        if (mWeatherData == null) {
            return 0;
        }
        return mWeatherData.length;
    }

    @Override
    public Object getItem(int position) {

        if (mWeatherData == null || position >= getCount()) {
            return null;
        }
        return mWeatherData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        WeatherResponse.WeatherData data = (WeatherResponse.WeatherData) getItem(position);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.list_item_forecast, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        WeatherResponse.WeatherData.Temperature temp = data.getTemp();
        double max = temp.getMax();
        double min =  temp.getMin();

        if (mUnit.equals("F")){
            max = max * (9/5) + 32;
            min = min * (9/5) + 32;

        }

        String date = Utility.getFriendlyDayString(mContext,data.getDt()*1000);
        viewHolder.maxTempTextView.setText(String.valueOf(max));
        viewHolder.minTempTextView.setText(String.valueOf(min));
        viewHolder.dateTextView.setText(date);

        WeatherResponse.WeatherData.Weather  weatherDescription = data.getWeather();
        viewHolder.weatherConditionTextView.setText(weatherDescription.getMain());


        return convertView;
    }

    private class ViewHolder{

        ImageView weatherImageView;
        TextView dateTextView;
        TextView weatherConditionTextView;
        TextView maxTempTextView;
        TextView minTempTextView;

        public ViewHolder(View view){
            weatherImageView = (ImageView) view.findViewById(R.id.weatherimage_view);
            dateTextView = (TextView) view.findViewById(R.id.day_text_view);
            weatherConditionTextView = (TextView) view.findViewById(R.id.weathertype_text_view);
            maxTempTextView = (TextView) view.findViewById(R.id.tempmax_text_view);
            minTempTextView = (TextView) view.findViewById(R.id.tempmin_text_view);

        }
    }
}
