package com.example.zkrasner.skibuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConditionDataStore extends ActionBarActivity {

    String slopeName;
    String currentUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_data);
        slopeName = getIntent().getExtras().getString("slopeName");
        currentUserName = getIntent().getExtras().getString("username");

        final TextView tv = (TextView) findViewById(R.id.slopeName);
        tv.setText(slopeName);

        // Find the proper trail from Parse and create a new trail object
        ParseQuery pq = new ParseQuery("trail");
        pq.whereEqualTo("name", slopeName);
        pq.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                int rating = object.getInt("rating");
                String ratingString = "" + rating;
                int difficulty = object.getInt("difficulty");

                // Use integer to find the right String
                String difficultyLevel = "";
                if (difficulty == 1) {
                    difficultyLevel = "Bunny Slope";
                } else if (difficulty == 2) {
                    difficultyLevel = "Green Circle";
                } else if (difficulty == 3) {
                    difficultyLevel = "Blue Square";
                } else if (difficulty == 4) {
                    difficultyLevel = "Black Diamond";
                } else if (difficulty == 5) {
                    difficultyLevel = "Double Black Diamond";
                }

                int conditionRating = object.getInt("condition");
                String condition = "";

                // Use integer to find the right String
                if (conditionRating == 1) {
                    condition = "Icy";
                } else if (difficulty == 2) {
                    condition = "Granular";
                } else if (difficulty == 3) {
                    condition = "Groomed";
                } else if (difficulty == 4) {
                    condition = "Packed Powder";
                } else if (difficulty == 5) {
                    condition = "Powder";
                }

                Trail t = new Trail(condition, ratingString, difficultyLevel);
                t.setName(slopeName);

                // Find the running average rating
                String updatedRating = TrailDataStore.returnRating(slopeName);
                String updatedCondition = TrailDataStore.returnCondition(slopeName);

                // Set the proper values
                TextView ratingText = (TextView) findViewById(R.id.rating);
                ratingText.setText(ratingString);
                TextView conditionText = (TextView) findViewById(R.id.condition);
                conditionText.setText(condition);
                TextView difficultyText = (TextView) findViewById(R.id.difficulty);

                difficultyText.setText(t.getDifficulty());
            }

        });


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

    public void addSlopeToFavorites(View view) {
        System.out.println("Add to favorites: " + currentUserName);

        ParseQuery favoriteQuery = new ParseQuery("accounts");
        favoriteQuery.whereEqualTo("username", currentUserName);
        favoriteQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    System.out.println("null object shitdick");
                }

                JSONArray jsonArr2 = object.getJSONArray("favoriteSlopes");
                if (jsonArr2 == null) {
                    jsonArr2 = new JSONArray();
                }

                JSONObject newTrail = new JSONObject();
                try {
                    newTrail.put("name", slopeName);
                    jsonArr2.put(newTrail);
                    object.put("favoriteSlopes", jsonArr2);
                    System.out.println("arr: " + jsonArr2);
                    object.saveInBackground();

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
