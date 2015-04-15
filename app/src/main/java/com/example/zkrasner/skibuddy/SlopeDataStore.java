package com.example.zkrasner.skibuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SlopeDataStore extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_data);
        String slopeName = getIntent().getExtras().getString("slopeName");
        TextView tv = (TextView) findViewById(R.id.slopeName);
        tv.setText(slopeName);

        Trail t = new Trail("icy", "4", "blue square");
        t.setName(slopeName);

        String rating = TrailDataStore.returnRating(slopeName);
        String condition = TrailDataStore.returnCondition(slopeName);

        TextView ratingText = (TextView) findViewById(R.id.rating);
        ratingText.setText(rating);
        TextView conditionText = (TextView) findViewById(R.id.condition);
        conditionText.setText(condition);
        TextView difficultyText = (TextView) findViewById(R.id.difficulty);



        difficultyText.setText(t.getDifficulty());




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slope_data, menu);
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
