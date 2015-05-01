package com.example.zkrasner.skibuddy;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TrailConditionActivity extends ActionBarActivity {

    String slopeName;
    String currentUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_data);
        slopeName = getIntent().getExtras().getString("slopeName");
        currentUserName = getIntent().getExtras().getString("username");
        final RelativeLayout background = (RelativeLayout) findViewById(R.id.fullLayout);
        final TextView tv = (TextView) findViewById(R.id.slopeName);
        tv.setText(slopeName);
        // Find the proper trail from Parse and create a new trail object
        ParseQuery pq = new ParseQuery("trail");
        pq.whereEqualTo("name", slopeName);
        pq.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                double rating = object.getDouble("rating");
                String ratingString = "Rating: " + rating;
                int difficulty = object.getInt("difficulty");

                // Use integer to find the right String
                String difficultyLevel = "";
                if (difficulty == 1) {
                    difficultyLevel = "Bunny Slope";
                    background.setBackgroundColor(Color.parseColor("#EBF0A1"));
                } else if (difficulty == 2) {
                    difficultyLevel = "Green Circle";
                    background.setBackgroundColor(Color.parseColor("#7BF0B4"));
                } else if (difficulty == 3) {
                    difficultyLevel = "Blue Square";
                    background.setBackgroundColor(Color.parseColor("#4EA2F0"));
                } else if (difficulty == 4) {
                    difficultyLevel = "Black Diamond";
                    background.setBackgroundColor(Color.parseColor("#4A545C"));
                } else if (difficulty == 5) {
                    difficultyLevel = "Double Black Diamond";
                    background.setBackgroundColor(Color.parseColor("#000000"));

                }

                int conditionRating = object.getInt("conditionRating");
                String condition = "";

                // Use integer to find the right String
                if (conditionRating == 0) {
                    condition = "Icy";
                } else if (difficulty == 1) {
                    condition = "Granular";
                } else if (difficulty == 2) {
                    condition = "Groomed";
                } else if (difficulty == 3) {
                    condition = "Packed Powder";
                } else if (difficulty == 4) {
                    condition = "Powder";
                }

                Trail t = new Trail(condition, ratingString, difficultyLevel);
                t.setName(slopeName);


                // Set the proper values
                TextView ratingText = (TextView) findViewById(R.id.rating);
                ratingText.setText(ratingString);
                TextView conditionText = (TextView) findViewById(R.id.condition);
                conditionText.setText("Conditions: " + condition);
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
        ParseQuery favoriteQuery = new ParseQuery("accounts");
        favoriteQuery.whereEqualTo("username", currentUserName);
        favoriteQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    System.out.println("null object");
                    return;
                }



                JSONArray jsonArr2 = object.getJSONArray("favoriteSlopes");
                if (jsonArr2 == null) {
                    jsonArr2 = new JSONArray();
                }
                boolean contains = false;
                for (int i = 0; i < jsonArr2.length(); i++) {
                    try {
                        if (jsonArr2.getJSONObject(i).getString("name").equalsIgnoreCase(slopeName)) {
                            contains = true;
                            return;
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

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
    public void removeSlopeFromFavorites(View view) {
        System.out.println("remove from favorites : " + slopeName);
        ParseQuery favoriteQuery = new ParseQuery("accounts");
        favoriteQuery.whereEqualTo("username", currentUserName);
        favoriteQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    System.out.println("null object");
                    return;
                }
                JSONArray slopeArr = object.getJSONArray("favoriteSlopes");

                JSONArray newSlopesArr = new JSONArray();
                if (object != null) {
                    for (int i = 0; i < slopeArr.length(); i++) {
                        try {
                            if (!slopeArr.getJSONObject(i).getString("name").equalsIgnoreCase(slopeName)) {
                                newSlopesArr.put(slopeArr.getJSONObject(i));
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
//                    slopeArr = newSlopesArr;
                }
                object.put("favoriteSlopes", newSlopesArr);
                object.saveInBackground();
            }
        });
    }
}
