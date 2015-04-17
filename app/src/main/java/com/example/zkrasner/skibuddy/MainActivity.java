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


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
    String mountainName;

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


        // Create a new Parse object
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

//        ParseObject killington = new ParseObject("Mountain");
//        killington.put("mountainName", "Killington");
//        killington.saveInBackground();

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        System.out.println(parent.getItemAtPosition(pos));
        mountainName = (String) parent.getItemAtPosition(pos);




        // find mountain by name
        ParseQuery query = new ParseQuery("Mountain");
        // MONT TREMBLANT HERE FOR SAMPLE
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
//                    System.out.println("found");
//                    String objName = (String) object.get("name");
//                    System.out.println(objName);
                } else {

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
        this.startActivity(i);
    }

    public void showSlopes(View view) {
        Intent i = new Intent(this, SlopeActivity.class);
        i.putExtra("mountain", mountainName);
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

}

