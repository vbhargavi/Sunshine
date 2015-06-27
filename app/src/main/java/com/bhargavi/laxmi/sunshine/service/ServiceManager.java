package com.bhargavi.laxmi.sunshine.service;

import com.bhargavi.laxmi.sunshine.service.data.WeatherResponse;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by laxmi on 6/25/15.
 */
public class ServiceManager {
    private static final String forecastUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&cnt=7" ;
  private static OkHttpClient client = new OkHttpClient();

    public static String makeCall(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static WeatherResponse getWeatherResponse(String q) throws IOException {

        String response = makeCall(forecastUrl + "&q=" + q);
        Gson gson = new Gson();
        WeatherResponse weatherResponse = gson.fromJson(response , WeatherResponse.class);
        return weatherResponse;
    }

}
