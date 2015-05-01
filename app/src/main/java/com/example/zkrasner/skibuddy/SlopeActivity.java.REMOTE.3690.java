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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class SlopeActivity extends ActionBarActivity {
    static String currentSlope;
    ListView listView;

    // The list of Trail objects
    ArrayList<Trail> trails;

    // The list of Lift objects
    ArrayList<Lift> lifts;

    // The list of trail names
    ArrayList<String> trailNames;

    // A way to save the context when creating GUI objects
    static SlopeActivity context;

    // The Mountain object
    Mountain mountain;

    // The Mountain name
    String mountainName;

    // The current username of the user
    static String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope);

        // Initialize arrays
        trailNames = new ArrayList<String>();
        trails = new ArrayList<Trail>();
        lifts = new ArrayList<Lift>();

        // Context is this
        context = this;
        // Set up the mountain object
        mountainName = getIntent().getExtras().getString("mountain");
        mountain = new Mountain(mountainName);

        currentUserName = getIntent().getExtras().getString("username");
        if (currentUserName != null) {
            System.out.println("CURRENT USERNAME: " + currentUserName);
        }
        else {
            System.out.println("null username");
        }

        listView = (ListView) findViewById(R.id.list);

        ParseQuery slopeQuery = new ParseQuery("Mountain");
        slopeQuery.whereEqualTo("name", mountainName);
        slopeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                JSONArray jsonArr = object.getJSONArray("trails");
                String[] arr = new String[jsonArr.length()];
                for (int i = 0; i < arr.length; i++) {
                    try {
                        final String trail = jsonArr.getJSONObject(i).getString("name");
                        trailNames.add(trail);
                        ParseQuery pq = new ParseQuery("trail");
                        pq.whereEqualTo("name", trail);
                        pq.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                int rating = object.getInt("rating");
                                String ratingString = "" + rating;
                                int difficulty = object.getInt("difficulty");
                                String difficultyLevel = getDifficulty(difficulty);

                                int conditionRating = object.getInt("condition");
                                String condition = getCondition(conditionRating);

                                Trail trailObject = new Trail(condition, ratingString, difficultyLevel);
                                trailObject.setName(trail);
                                trails.add(trailObject);
                            }

                        });
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

                mountain.addTrails(trails);

                for (int i = 0; i < trailNames.size(); i++) {
                    arr[i] = trailNames.get(i);
                }

                if (!TrailDataStore.added_Once) {
                    for (int i = 0; i < trailNames.size(); i++) {
                        TrailDataStore.insertTrail(trailNames.get(i));
                    }
                    TrailDataStore.added_Once = true;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
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

                jsonArr = object.getJSONArray("lifts");
                for (int i = 0; i < jsonArr.length(); i++) {
                    try {
                        final String lift = jsonArr.getJSONObject(i).getString("name");
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
                                lifts.add(liftObject);
                            }

                        });
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                mountain.addLifts(lifts);
            }
        });


    }

    // Getting the correct condition String
    private String getCondition(int conditionRating) {
        String condition = "";
        if (conditionRating == 1) {
            condition = "Icy";
        } else if (conditionRating == 2) {
            condition = "Granular";
        } else if (conditionRating == 3) {
            condition = "Groomed";
        } else if (conditionRating == 4) {
            condition = "Packed Powder";
        } else if (conditionRating == 5) {
            condition = "Powder";
        }
        return condition;
    }

    // Getting the correct difficulty String
    private String getDifficulty(int difficulty) {
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
        return difficultyLevel;
    }

    public static void showSlopeData(View view) {
        Intent i = new Intent(context, ConditionDataStore.class);
        i.putExtra("slopeName", currentSlope);
        i.putExtra("username", currentUserName);
        context.startActivity(i);
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
        Intent i = new Intent(this, ReportTrailConditions.class);
        i.putExtra("mtnName", mountainName);
        this.startActivity(i);
    }
}
