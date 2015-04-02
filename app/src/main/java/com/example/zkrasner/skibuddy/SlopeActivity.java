package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class SlopeActivity extends ActionBarActivity {
    String currentSlope;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_time);
        String mountainName = getIntent().getExtras().getString("mountain");

        ArrayList<Trail> t = new ArrayList<Trail>();
        Trail a = new Trail("icy", "3", "blue square");
        a.setName("Lowell Thomas");
        Trail b = new Trail("powdah", "5", "black diamond");
        b.setName("Glades brah");
        t.add(a);
        t.add(b);

        ArrayList<Lift> l = new ArrayList<Lift>();
        Lift l1 = new Lift();
        l1.setCapacity(3);
        l1.setDuration(10.00);
        l1.setWaitTime(12.00);
        l1.setName("lift 1");

        Lift l2 = new Lift();
        l2.setCapacity(3);
        l2.setDuration(4.00);
        l2.setWaitTime(5.00);
        l2.setName("lift 2");
        l.add(l1);
        l.add(l2);


        Mountain mountain = new Mountain("Killington");
        mountain.addTrails(t);
        mountain.addLifts(l);

        // get lifts and wait times, put into strings to display in list
        String[] times = new String[mountain.getLifts().size()];
        for (int i = 0; i < mountain.getTrails().size(); i++) {
            Trail fick = mountain.getTrails().get(i);
            String name = fick.getName();
            times[i] = name;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentSlope = (String) adapterView.getItemAtPosition(i);
                showSlopeData(view);
            }
        });
    }

    public void showSlopeData(View view) {
        Intent i = new Intent(this, SlopeData.class);
        i.putExtra("slopeName", currentSlope);
        this.startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slope, menu);
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

    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }
}
