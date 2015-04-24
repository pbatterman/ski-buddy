package com.example.zkrasner.skibuddy;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sfreeman2494 on 4/2/15.
 */
public class RetrieveWeatherData extends AsyncTask<String, Void, ArrayList<String>> {

    private Exception exception;

    @Override
    protected ArrayList<String> doInBackground(String... city) {


        OwmClient owm = new OwmClient();
        WeatherStatusResponse currentWeather = null;
        ArrayList<String> list = new ArrayList<String>();
        String cond = null;
        Float temp;
        try {
            if (city[0].equals("Killington")) {
                currentWeather = owm.currentWeatherAtCity("Rutland");
            } else if (city[0].equals("Pico")) {
                currentWeather = owm.currentWeatherAtCity("Rutland");
            } else {
                currentWeather = owm.currentWeatherAtCity("Boston");
            }
            System.out.println(currentWeather.getWeatherStatus().get(0).getCoord().getLatitude());
            System.out.println(currentWeather.getWeatherStatus().get(0).getCoord().getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (currentWeather.hasWeatherStatus()) {
            System.out.println(currentWeather.getWeatherStatus().get(0).getWeatherConditions().get(0).getDescription());
            cond = currentWeather.getWeatherStatus().get(0).getWeatherConditions().get(0).getDescription();
            temp = currentWeather.getWeatherStatus().get(0).getTemp();
            temp = (((temp - 273) * 9.0f) / 5.0f) + 32.0f;
            String tempString = temp.toString();
            tempString = tempString.substring(0, 4);
            list.add(cond);
            list.add(tempString);

        }
        return list;
    }
}