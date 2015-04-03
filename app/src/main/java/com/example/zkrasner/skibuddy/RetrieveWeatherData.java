package com.example.zkrasner.skibuddy;

import android.os.AsyncTask;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by sfreeman2494 on 4/2/15.
 */
public class RetrieveWeatherData extends AsyncTask<String, Void, String> {

    private Exception exception;

    @Override
    protected String doInBackground(String... city) {
        OwmClient owm = new OwmClient();
        WeatherStatusResponse currentWeather = null;
        String ret = null;
        try {
            currentWeather = owm.currentWeatherAtCity(city[0]);
            System.out.println(currentWeather.getWeatherStatus().get(0).getCoord().getLatitude());
            System.out.println(currentWeather.getWeatherStatus().get(0).getCoord().getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (currentWeather.hasWeatherStatus()) {
            System.out.println(currentWeather.getWeatherStatus().get(0).getWeatherConditions().get(0).getDescription());
            ret = currentWeather.getWeatherStatus().get(0).getWeatherConditions().get(0).getDescription();
        }
        return ret;
    }
}