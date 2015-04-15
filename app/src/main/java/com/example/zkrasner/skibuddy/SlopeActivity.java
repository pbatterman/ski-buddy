package com.example.zkrasner.skibuddy;

import android.content.Intent;
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
    ArrayList<Trail> t;
    ArrayList<String> trailNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope);
        String mountainName = getIntent().getExtras().getString("mountain");
        trailNames = new ArrayList<String>();
       t = new ArrayList<Trail>();
        Trail a = new Trail("Icy", "3", "blue square");
        a.setName("Lowell Thomas");
        Trail b = new Trail("Powder", "5", "black diamond");
        b.setName("Glades");
        Trail d = new Trail("the worst", "5", "black diamond");
        b.setName("Cliff Run");

        t.add(a);
        t.add(b);
        t.add(d);

        if(!TrailDataStore.added_Once) {
            TrailDataStore.insertTrail("Lowell Thomas");
            TrailDataStore.insertTrail("Glades");
            TrailDataStore.insertTrail("Cliff Run");
            TrailDataStore.added_Once = true;
        }



        trailNames.add("Lowell Thomas");
        trailNames.add("Glades");
        trailNames.add("Cliff Run");

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

        Lift l3 = new Lift();
        l3.setCapacity(3);
        l3.setDuration(4.00);
        l3.setWaitTime(5.00);
        l3.setName("lift 3");
        l.add(l3);




        Mountain mountain = new Mountain("Killington");
        mountain.addTrails(t);
        mountain.addLifts(l);

        // get lifts and wait times, put into strings to display in list
        String[] times = new String[mountain.getLifts().size()];
        for (int i = 0; i < mountain.getTrails().size(); i++) {
            Trail testTrail = mountain.getTrails().get(i);
            String name = testTrail.getName();
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

        String[] arr = {"Lowell Thomas", "Glades", "Cliff Run"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arr);


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
        Intent i = new Intent(this, SlopeDataStore.class);
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

    public void reportTimes(View view) {
        String mountainNameString = getIntent().getExtras().getString("mountain");
        Intent i = new Intent(this, ReportTrailConditions.class);
        i.putExtra("mtnName", mountainNameString);
        i.putStringArrayListExtra("trails", trailNames);
        this.startActivity(i);
    }
}
