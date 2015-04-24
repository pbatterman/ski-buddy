package com.example.zkrasner.skibuddy;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MountainConditionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_condition);
        String mountainName = getIntent().getExtras().getString("mountain");
        AsyncTask<String, Void, ArrayList<String>> task = new RetrieveWeatherData().execute(mountainName);
        ArrayList<String> currentWeather = null;
        try {
            currentWeather = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        TextView mountainText = (TextView) findViewById(R.id.mountain_name);
        mountainText.setText(mountainName);
        TextView weatherText = (TextView) findViewById(R.id.weather);
        weatherText.setText(currentWeather.get(0));
        TextView tempText = (TextView) findViewById(R.id.temperature_number);
        tempText.setText(currentWeather.get(1));

        final RequestQueue queue = Volley.newRequestQueue(this);
        ParseQuery woiedQuery = new ParseQuery("Mountain");
        // need to get mountainName from mainActivity intent
        woiedQuery.whereEqualTo("name", mountainName);
        woiedQuery.getFirstInBackground(new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // get image
                    String wid = object.getString("woeid");
                    String url ="http://weather.yahooapis.com/forecastrss?w=" + wid + "&u=f";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    System.out.println("Response is: " + response.substring(0, 500));
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("That didn't work!");
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);



                } else {

                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mountain_condition, menu);
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
}
