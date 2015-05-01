package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import java.util.Arrays;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class WaitTimeActivity extends ActionBarActivity {
    ListView listView;
    ArrayList<Lift> lifts;
    String mountainName;
    String[] startTimes;
    Mountain mountain;
    Context contextview;
    WaitTimeActivity context;

    ArrayList<String> liftNames = new ArrayList<String>();
    String[] out;
    int count;
    ArrayList<String> toFill;
    int selectedItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextview = getApplicationContext();
        System.out.println("was created");
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


        reValidate();

        final Button button = (Button) findViewById(R.id.refreshButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               refresh();
            }
        });

    }


    public void reValidate(){


        toFill = new ArrayList<String>();
        final int last = liftNames.size() - 1;
        count = 0;
        for(String l : liftNames) {
            System.out.println(l);
            ParseQuery pq = new ParseQuery("Lift");
            pq.whereEqualTo("name", l);
            pq.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        final String a = ( (String) (object.get("name") + ": " + object.get("waitTime")));
                        toFill.add(a);

                        if(count ==  last){
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(contextview,
                                    R.layout.listviewlayout, toFill);
                            ListView lv = (ListView) findViewById(R.id.list);
                            lv.setAdapter(adapter);
                            System.out.println(toFill.toString());

                        }
                        count++;

                    }

                }


            });

        }



    }

    public void addToArrayList(String s){
        toFill.add(s);
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
        reValidate();
    }

    public void onSubmitButtonSelected(View view) {
        EditText e = (EditText) findViewById(R.id.numberTextBox);
        String intermediate = e.getText().toString();
        int to_add;
        if (intermediate.length() == 0) {
            return;
        } else {
            intermediate = e.getText().toString();

            to_add = Integer.parseInt(intermediate);
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
                    object.put("waitTime",add);
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
