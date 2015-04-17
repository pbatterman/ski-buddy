package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.content.res.Resources;
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
    ArrayList<Lift> l;
    String mountainName;
    Mountain mountain;
    WaitTimeActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_time);
        mountainName = getIntent().getExtras().getString("mountain");



        TextView tv = (TextView) findViewById(R.id.mountainName);
        tv.setText(mountainName);

        mountain = new Mountain(mountainName);
        l = new ArrayList<Lift>();

        ParseQuery pq = new ParseQuery("Mountain");
        pq.whereEqualTo("name", mountainName);
        pq.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                JSONArray jsonArr = object.getJSONArray("lifts");
                for (int i = 0; i < jsonArr.length(); i++) {
                    try {
                        final String lift = jsonArr.getJSONObject(i).getString("name");
                        System.out.println(lift);
                        ParseQuery pq = new ParseQuery("Lift");
                        pq.whereEqualTo("name", lift);
                        pq.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                double duration = object.getDouble("duration");
                                double waitTime = object.getDouble("waitTime");
                                int capacity = object.getInt("capacity");

                                Lift liftObject = new Lift();
                                liftObject.setCapacity(capacity);
                                liftObject.setDuration(duration);
                                liftObject.setWaitTime(waitTime);
                                liftObject.setName(lift);
                                l.add(liftObject);
                            }

                        });
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                mountain.addLifts(l);

                ArrayList<String> arr = new ArrayList<String>();
                for(int i = 0; i < l.size(); i++) {
                    arr.add(l.get(i).getName());
                }
                /*
                Spinner spinner = (Spinner) findViewById(R.id.liftSpinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arr); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
                */

                Spinner spinner = (Spinner) findViewById(R.id.liftSpinner);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context,
                        R.array.lift_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter2);

                // get lifts and wait times, put into strings to display in list
                String[] times = new String[mountain.getLifts().size()];
                for (int i = 0; i < mountain.getLifts().size(); i++) {
                    Lift testLift = mountain.getLifts().get(i);
                    String waitTime = testLift.getName() + ": " + testLift.getWaitTime();
                    times[i] = waitTime;
                }

                listView = (ListView) findViewById(R.id.list);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, times);


                // Assign adapter to ListView
                listView.setAdapter(adapter);
            }
        });



    }


    public void reValidate(){
        mountain = new Mountain(mountainName);
        mountain.addLifts(l);

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



    public void onTestButtonSelected(View view){
        EditText e = (EditText) findViewById(R.id.numberTextBox);
        String intermediate = e.getText().toString();
        int to_add;
        if(intermediate.length() == 0){
            to_add = 0;
        }else {
            to_add = Integer.parseInt(intermediate);
        }



        Spinner mySpinner=(Spinner) findViewById(R.id.liftSpinner);
        String text = mySpinner.getSelectedItem().toString();
        if(text.equals("Fast Lift")){
            Lift tmp = l.get(0);
            tmp.injectHotStickyWaitTime(to_add);
            reValidate();
            return;
        }

        Lift tmp = l.get(1);
        tmp.injectHotStickyWaitTime(to_add);


        reValidate();

    }

    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }
}
