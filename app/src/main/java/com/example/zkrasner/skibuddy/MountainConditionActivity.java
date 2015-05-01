package com.example.zkrasner.skibuddy;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.GetCallback;
import com.parse.ParseException;
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

        final RelativeLayout background = (RelativeLayout) findViewById(R.id.conditionBackground);


        /* AsyncTask<String, Void, ArrayList<String>> task = new RetrieveWeatherData().execute(mountainName);
        ArrayList<String> currentWeather = null;
        try {
            currentWeather = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        TextView mountainText = (TextView) findViewById(R.id.mountain_name);
        mountainText.setText(mountainName);
        TextView tempText = (TextView) findViewById(R.id.temperature_number);
        tempText.setText("0");

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

                                        // Searching for weather condition
                                    String target = "<yweather:condition  text=\"";
                                    String end = "\"";
                                    String weather = parseText(response, target, end);
                                    TextView weatherText = (TextView) findViewById(R.id.weather);
                                    weatherText.setText(weather);


                                        // Searching for temperature
                                    target = "temp=\"";
                                    String temperature = parseText(response, target, end);
                                    TextView tempText = (TextView) findViewById(R.id.temperature_number);
                                    tempText.setText(temperature);

                                    int temp = Integer.parseInt(temperature);
                                    if (temp < 10) {
                                        background.setBackgroundColor(Color.parseColor("#5B8CD6"));
                                    }
                                    else if (temp < 32) {
                                        background.setBackgroundColor(Color.parseColor("#73C2FF"));
                                    }
                                    else if (temp < 40) {
                                        background.setBackgroundColor(Color.parseColor("#8FDBFF"));
                                    }
                                    else if (temp < 50) {
                                        background.setBackgroundColor(Color.parseColor("#8DCCE3"));
                                    }
                                    else if (temp < 70) {
                                        background.setBackgroundColor(Color.parseColor("#A7CCB6"));
                                    }
                                    else if (temp < 90) {
                                        background.setBackgroundColor(Color.parseColor("#FFF0A8"));
                                    }
                                    else {
                                        background.setBackgroundColor(Color.parseColor("#FFB55E"));

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("That didn'trails work!");
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);



                } else {

                }

            }
        });


    }

    // A function that returns a substring between two substrings
    private String parseText(String str, String preString, String postString) {

        String found = "";
        int begin = str.indexOf(preString);
        if (begin == -1) {
            return "";
        }
        begin += preString.length();
        int end = str.indexOf(postString, begin);

        if (end == -1) {
            return "";
        }

        return str.substring(begin, end);
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
