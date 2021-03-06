package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.DecimalFormat;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.content.Context;

import java.util.ArrayList;


public class WaitTimeActivity extends ActionBarActivity {
    String mountainName;
    Context contextView;
    WaitTimeActivity context;

    ArrayList<String> liftNames = new ArrayList<String>();
    int count;
    ArrayList<String> toFill;
    ArrayList<Double> toFillHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextView = getApplicationContext();
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_time);
        mountainName = getIntent().getExtras().getString("mountain");
        liftNames = getIntent().getExtras().getStringArrayList("lifts");

        Spinner spinner = (Spinner) findViewById(R.id.liftSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liftNames);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        revalidate();

        final Button button = (Button) findViewById(R.id.refreshButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               refresh();
            }
        });

    }


    public void revalidate(){


        toFill = new ArrayList<String>();
        toFillHelper = new ArrayList<Double>();
        final int last = liftNames.size() - 1;
        count = 0;
        for(String l : liftNames) {
            ParseQuery pq = new ParseQuery("Lift");
            pq.whereEqualTo("name", l);
            pq.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        Double wt = object.getDouble("waitTime");
                        DecimalFormat df = new DecimalFormat("#.##");
                        String shortWt = df.format(wt);
                        final String a = ( (String) (object.get("name") + ": " + shortWt + " min"));
                        toFill.add(a);
                        toFillHelper.add(object.getDouble("waitTime"));

                        if(count ==  last){

                            // sort by wait times

                            for(int i = 0; i < toFill.size() - 1; i++) {

                                double smallest = toFillHelper.get(i);
                                int index = i;

                                for (int j = i+1; j < toFill.size(); j++) {
                                    if(toFillHelper.get(j) < smallest){
                                        index = j;
                                        smallest = toFillHelper.get(j);
                                    }


                                }

                                if(index != i){
                                    // swap i and index
                                    String tmp = toFill.get(i);

                                    toFill.set(i,toFill.get(index));
                                    toFill.set(index,tmp);

                                    Double curr = toFillHelper.get(i);

                                    toFillHelper.set(i,toFillHelper.get(index));
                                    toFillHelper.set(index,curr);


                                }


                            }




                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(contextView,
                                    R.layout.listviewlayout, toFill);
                            ListView lv = (ListView) findViewById(R.id.list);
                            lv.setAdapter(adapter);

                        }
                        count++;

                    }

                }


            });

        }



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


    public void refresh(){
        revalidate();
    }

    public void onSubmitButtonSelected(View view) {
        EditText e = (EditText) findViewById(R.id.numberTextBox);
        String intermediate = e.getText().toString();
        int to_add;
        if (intermediate.length() == 0) {
            return;
        } else {
            intermediate = e.getText().toString();

            try {
                to_add = Integer.parseInt(intermediate);
            }catch (Exception e1){
                return;
            }
        }
        Spinner mySpinner = (Spinner) findViewById(R.id.liftSpinner);

        String search = mySpinner.getSelectedItem().toString();

        final int add = to_add;

        // search for the lift in parse
        ParseQuery pq = new ParseQuery("Lift");
        pq.whereEqualTo("name", search);
        pq.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null){
                    Integer numEntries = object.getInt("numEntries");
                    Double existingTime = object.getDouble("waitTime");
                    Double newTime = ((existingTime * numEntries) + add) / (numEntries + 1);
                    object.put("waitTime", newTime);
                    object.put("numEntries", numEntries + 1);
                    object.saveInBackground();

                }

            }
        });

    }

    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }

}
