package com.bhargavi.laxmi.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhargavi.laxmi.sunshine.service.data.WeatherResponse;
import com.squareup.okhttp.internal.Util;

/**
 * Created by laxmi on 6/30/15.
 */
public class WeatherAdapter extends BaseAdapter {

    private WeatherResponse.WeatherData[] mWeatherData;
    private Context mContext;
    private LayoutInflater mInflater;
    private String mUnit;
    private boolean isMetric;
    private boolean mIsTablet;

    public WeatherAdapter(Context context, WeatherResponse.WeatherData[] weatherData, String unit, boolean isTablet) {
        mWeatherData = weatherData;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mUnit = unit;
        isMetric = mUnit.equals("C");
        mIsTablet = isTablet;

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
        ViewHolder viewHolder;
        FirstViewHolder firstViewHolder;
        WeatherResponse.WeatherData data = (WeatherResponse.WeatherData) getItem(position);
        WeatherResponse.WeatherData.Temperature temp = data.getTemp();
        double max = temp.getMax();
        double min = temp.getMin();
        String maxTempString = Utility.formatTemperature(mContext, max, isMetric);
        String minTempString = Utility.formatTemperature(mContext, min, isMetric);

        if (!isMetric) {
            max = max * (9 / 5) + 32;
            min = min * (9 / 5) + 32;

        }

        String date = Utility.getFriendlyDayString(mContext, data.getDt() * 1000);
        WeatherResponse.WeatherData.Weather weatherDescription = data.getWeather();

        int weatherId = weatherDescription.getId();

        if (!mIsTablet && position == 0) {
            if (convertView == null || convertView.getTag() instanceof ViewHolder) {
                convertView = mInflater.inflate(R.layout.list_first_item_forecast, parent, false);
                firstViewHolder = new FirstViewHolder(convertView);
                convertView.setTag(firstViewHolder);

            } else {
                firstViewHolder = (FirstViewHolder) convertView.getTag();
            }

            firstViewHolder.maxTempTextView.setText(maxTempString);
            firstViewHolder.minTempTextView.setText(minTempString);
            firstViewHolder.dateTextView.setText(date);
            firstViewHolder.weatherConditionTextView.setText(weatherDescription.getMain());
            int resourceId = Utility.getArtResourceForWeatherCondition(weatherId);
            firstViewHolder.weatherImageView.setImageResource(resourceId);


        } else {


            if (convertView == null || convertView.getTag() instanceof FirstViewHolder) {
                convertView = mInflater.inflate(R.layout.list_item_forecast, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }

            viewHolder.maxTempTextView.setText(maxTempString);
            viewHolder.minTempTextView.setText(minTempString);
            viewHolder.dateTextView.setText(date);
            viewHolder.weatherConditionTextView.setText(weatherDescription.getMain());
            int resourceId = Utility.getIconResourceForWeatherCondition(weatherId);
            viewHolder.weatherImageView.setImageResource(resourceId);

        }




        return convertView;
    }

    private class ViewHolder {

        ImageView weatherImageView;
        TextView dateTextView;
        TextView weatherConditionTextView;
        TextView maxTempTextView;
        TextView minTempTextView;

        public ViewHolder(View view) {
            weatherImageView = (ImageView) view.findViewById(R.id.weatherimage_view);
            dateTextView = (TextView) view.findViewById(R.id.day_text_view);
            weatherConditionTextView = (TextView) view.findViewById(R.id.weathertype_text_view);
            maxTempTextView = (TextView) view.findViewById(R.id.tempmax_text_view);
            minTempTextView = (TextView) view.findViewById(R.id.tempmin_text_view);

        }
    }

    private class FirstViewHolder {

        ImageView weatherImageView;
        TextView dateTextView;
        TextView weatherConditionTextView;
        TextView maxTempTextView;
        TextView minTempTextView;

        public FirstViewHolder(View view) {
            weatherImageView = (ImageView) view.findViewById(R.id.weatherimage_view);
            dateTextView = (TextView) view.findViewById(R.id.day_text_view);
            weatherConditionTextView = (TextView) view.findViewById(R.id.weathertype_text_view);
            maxTempTextView = (TextView) view.findViewById(R.id.tempmax_text_view);
            minTempTextView = (TextView) view.findViewById(R.id.tempmin_text_view);

        }
    }

}
