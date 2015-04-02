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
public class RetrieveWeatherData extends AsyncTask<String, Void, Void> {

    private Exception exception;

    @Override
    protected Void doInBackground(String... city) {
        OwmClient owm = new OwmClient();
        WeatherStatusResponse currentWeather = null;
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
            WeatherData weather = currentWeather.getWeatherStatus().get(0);
            if (weather.getPrecipitation() == Integer.MIN_VALUE) {
                WeatherData.WeatherCondition weatherCondition = weather.getWeatherConditions().get(0);
                String description = weatherCondition.getDescription();
                if (description.contains("rain") || description.contains("shower"))
                    System.out.println("No rain measures in " + city[0] + "  but reports of " + description);
                else
                    System.out.println("No rain measures in " + city[0] + ": " + description);
            } else
                System.out.println("It's raining in " + city[0] + ": " + weather.getPrecipitation() + " mm/h");
        }
        return null;
    }
}