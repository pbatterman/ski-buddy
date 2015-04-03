package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.bitpipeline.lib.owm.WeatherData.WeatherCondition;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
    String mountainName;

    static String currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.mountains_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mountains_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        System.out.println(parent.getItemAtPosition(pos));
        mountainName = (String) parent.getItemAtPosition(pos);
        TextView weatherText = (TextView) findViewById(R.id.weather);
            AsyncTask<String, Void, String> task = new RetrieveWeatherData().execute("Rutland");
        String currentWeather = null;
        try {
            currentWeather = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        weatherText.setText(currentWeather);
    }

    public static void setCurrentWeather(String currentWeather) {
        currentWeather = currentWeather;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLiftTimes(View view) {
        Intent i = new Intent(this, waitTimeActivity.class);
        i.putExtra("mountain", mountainName);
        this.startActivity(i);
    }

    public void showSlopes(View view) {
        Intent i = new Intent(this, SlopeActivity.class);
        i.putExtra("mountain", mountainName);
        this.startActivity(i);
    }
}

