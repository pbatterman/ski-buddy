package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import java.util.List;


public class LoadActivity extends ActionBarActivity {
    ArrayList<String> mountains;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Parse.initialize(this, "q3TW5WjUYJCEHqn4J2RVb8sscM8ZCIWZNQt7acL1", "umavEy2ekBXFPCfy7CGKQp9h3sOOf9AOClKThJri");


        List<ParseObject> ob = null;

        mountains = new ArrayList<String>();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Mountain");
        query.orderByAscending("createdAt");
        try {
            ob = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (ob != null) {
            for (int i = 0; i < ob.size(); i++) {
                mountains.add((String) ob.get(i).get("name"));
            }
        }


        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("mountains", mountains);
        this.startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load, menu);
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

}
