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

    // List containing the Lift objects
    ArrayList<Lift> lifts = new ArrayList<Lift>();

    // List contatining the names of the lifts
    ArrayList<String> liftNames = new ArrayList<String>();

    // List containing the wait times
    ArrayList<String> times = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "q3TW5WjUYJCEHqn4J2RVb8sscM8ZCIWZNQt7acL1", "umavEy2ekBXFPCfy7CGKQp9h3sOOf9AOClKThJri");


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
                    System.out.println("filename: " + fname);
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

        pq.whereEqualTo("name", mountainName);
        pq.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
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
        Mountain mountain = new Mountain(mountainName);

        mountain.addLifts(lifts);

        // get lifts and wait times, put into strings to display in list
        String[] times = new String[mountain.getLifts().size()];
        for (int j = 0; j < mountain.getLifts().size(); j++) {
            Lift lift = mountain.getLifts().get(j);
            String waitTime = lift.getName() + ": " + lift.getWaitTime();
            times[j] = waitTime;
        }
        i.putExtra("liftStrings", times);
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
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("mountain", mountainName);
        this.startActivity(i);
    }

    public void showUserPage(View view) {
        Intent i = new Intent(this, UserActivity.class);
        currentUserName = LoginActivity.getCurrentUserName();
        i.putExtra("username", currentUserName);
        this.startActivity(i);
    }

}

