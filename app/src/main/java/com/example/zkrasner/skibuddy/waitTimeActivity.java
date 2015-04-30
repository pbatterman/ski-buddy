package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class WaitTimeActivity extends ActionBarActivity {
    ListView listView;
    ArrayList<Lift> lifts;
    String mountainName;
    String[] startTimes;
    Mountain mountain;
    WaitTimeActivity context;

    final ArrayList<String> liftNames = new ArrayList<String>();
    final ArrayList<String> times = new ArrayList<String>();
    int selectedItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_time);
        mountainName = getIntent().getExtras().getString("mountain");
        startTimes = getIntent().getExtras().getStringArray("liftStrings");


        TextView tv = (TextView) findViewById(R.id.mountainName);
        tv.setText(mountainName);

        mountain = new Mountain(mountainName);
        lifts = new ArrayList<Lift>();

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
                final Spinner liftSpinner = (Spinner) findViewById(R.id.liftSpinner);
                ArrayAdapter<String> liftSpinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, liftNames); //selected item will look like a spinner set from XML
                liftSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                liftSpinner.setAdapter(liftSpinnerArrayAdapter);


                liftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        selectedItemIndex = liftSpinner.getSelectedItemPosition();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedItemIndex = 0;
                    }
                });

                // get lifts and wait times, put into strings to display in list
                listView = (ListView) findViewById(R.id.list);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, startTimes);


                // Assign adapter to ListView
                listView.setAdapter(adapter);

            }
        });

    }


    public void reValidate(){
        mountain = new Mountain(mountainName);
        mountain.addLifts(lifts);

        // get lifts and wait times, put into strings to display in list
        String[] times = new String[mountain.getLifts().size()];
        for (int i = 0; i < mountain.getLifts().size(); i++) {
            Lift lift = mountain.getLifts().get(i);
            String waitTime = lift.getName() + ": " + lift.getWaitTime();
            times[i] = waitTime;
        }

        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, times);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wait_time, menu);
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

    public void onSubmitButtonSelected(View view){
        EditText e = (EditText) findViewById(R.id.numberTextBox);
        String intermediate = e.getText().toString();
        int to_add;
        if(intermediate.length() == 0){
            to_add = 0;
        } else {
            to_add = Integer.parseInt(intermediate);
        }
        Spinner mySpinner = (Spinner) findViewById(R.id.liftSpinner);
        int i = mySpinner.getSelectedItemPosition();
        Lift tmp = lifts.get(0);
        tmp.insertWaitTime(to_add);
        reValidate();
    }

    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }
}
