package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
    String mountainName;
    String currentUserName;

    static ArrayList<Double> out;

    // List containing the Lift objects
    static ArrayList<Lift> lifts = new ArrayList<Lift>();

    // List contatining the names of the lifts
    ArrayList<String> liftNames = new ArrayList<String>();

    // List containing the wait times
    ArrayList<String> times = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> mountains = getIntent().getExtras().getStringArrayList("mountains");


        Spinner spinner = (Spinner) findViewById(R.id.mountains_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mountains);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        mountainName = (String) parent.getItemAtPosition(pos);




        // find mountain by name
        ParseQuery query = new ParseQuery("Mountain");


        // find and display trail map
        query.whereEqualTo("name", mountainName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // get image
                    final ParseImageView imageView = (ParseImageView) findViewById(R.id.mountain_image);
                    final ParseFile imageFile = object.getParseFile("trailmap");
                    String fname = imageFile.getName();
                    imageFile.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                imageView.setParseFile(imageFile);
                                imageView.loadInBackground(new GetDataCallback() {
                                    public void done(byte[] data, ParseException e) {
                                    }
                                });

                                // data has the bytes for the image
                            } else {
                                // something went wrong
                            }
                        }
                    });
                } else {

                }

            }
        });

        ParseQuery pq = new ParseQuery("Mountain");
        lifts = new ArrayList<Lift>();
        liftNames = new ArrayList<String>();
        pq.whereEqualTo("name", mountainName);
        pq.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object != null) {
                    JSONArray jsonArr = object.getJSONArray("lifts");

                    for (int i = 0; i < jsonArr.length(); i++) {
                        try {
                            final String lift = jsonArr.getJSONObject(i).getString("name");
                            ParseQuery pq = new ParseQuery("Lift");
                            pq.whereEqualTo("name", lift);
                            pq.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    double duration = object.getDouble("duration");
                                    double waitTime = object.getDouble("waitTime");
                                    times.add("" + waitTime);
                                    int capacity = object.getInt("capacity");

                                    Lift liftObject = new Lift();
                                    liftObject.setCapacity(capacity);
                                    liftObject.setDuration(duration);
                                    liftObject.setWaitTime(waitTime);
                                    liftObject.setName(lift);
                                    lifts.add(liftObject);
                                    liftNames.add(liftObject.getName());
                                }
                            });
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
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
        Intent i = new Intent(this, WaitTimeActivity.class);
        i.putExtra("mountain", mountainName);

        i.putExtra("lifts", liftNames);
        this.startActivity(i);
    }

    public void showSlopes(View view) {
        Intent i = new Intent(this, SlopeActivity.class);
        i.putExtra("mountain", mountainName);
        currentUserName = LoginActivity.getCurrentUserName();
        if (currentUserName != null) {
            i.putExtra("username", currentUserName);
        }
        this.startActivity(i);
    }

    public void showLogin(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        this.startActivity(i);
    }

    public void showMountainConditions(View view) {
        Intent i = new Intent(this, MountainConditionActivity.class);
        i.putExtra("mountain", mountainName);
        this.startActivity(i);
    }

    public void showUserPage(View view) {
        Intent i = new Intent(this, UserActivity.class);
        currentUserName = LoginActivity.getCurrentUserName();
        i.putExtra("username", currentUserName);
        this.startActivity(i);
    }

    public static ArrayList<Lift> getLifts() {
        return lifts;
    }

    public static ArrayList<Double> getWaitTimes(ArrayList<String> l) {

        out = new ArrayList<Double>();
        for (String a : l) {
            ParseQuery pq = new ParseQuery("Lift");
            pq.whereEqualTo("name", l);
            pq.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if(object != null) {
                        double waitTime = object.getDouble("waitTime");
                        out.add(waitTime);
                    }
                }
            });
        }

        return out;
    }

}

