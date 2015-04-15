package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class WaitTimeActivity extends ActionBarActivity {
    ListView listView;
    ArrayList<Trail> t;
    ArrayList<Lift> l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_time);
        String mountainName = getIntent().getExtras().getString("mountain");


        Spinner spinner = (Spinner) findViewById(R.id.liftSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.lift_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter2);



        TextView tv = (TextView) findViewById(R.id.mountainName);
        tv.setText(mountainName);

        t = new ArrayList<Trail>();
        Trail a = new Trail("icy", "3", "blue square");
        Trail b = new Trail("powdah", "5", "black diamond");
        t.add(a);
        t.add(b);

        l = new ArrayList<Lift>();
        Lift l1 = new Lift();
        l1.setCapacity(3);
        l1.setDuration(10.00);
        l1.setWaitTime(12.00);
        l1.setName("Fast Lift");

        Lift l2 = new Lift();
        l2.setCapacity(3);
        l2.setDuration(4.00);
        l2.setWaitTime(5.00);
        l2.setName("Slow Bunny Hill");
        l.add(l1);
        l.add(l2);


        Mountain mountain = new Mountain("Killington");
        mountain.addTrails(t);
        mountain.addLifts(l);

        // get lifts and wait times, put into strings to display in list
        String[] times = new String[mountain.getLifts().size()];
        for (int i = 0; i < mountain.getLifts().size(); i++) {
            Lift testLift = mountain.getLifts().get(i);
            String waitTime = testLift.getName() + ": " + testLift.getWaitTime();
            times[i] = waitTime;
        }

        listView = (ListView) findViewById(R.id.list);

        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, times);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }


    public void reValidate(){
        Mountain mountain = new Mountain("Killington");
        mountain.addTrails(t);
        mountain.addLifts(l);

        // get lifts and wait times, put into strings to display in list
        String[] times = new String[mountain.getLifts().size()];
        for (int i = 0; i < mountain.getLifts().size(); i++) {
            Lift fick = mountain.getLifts().get(i);
            String waitTime = fick.getName() + ": " + fick.getWaitTime();
            times[i] = waitTime;
        }

        listView = (ListView) findViewById(R.id.list);

        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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
