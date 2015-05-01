package com.example.zkrasner.skibuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserActivity extends ActionBarActivity {
    private static ListView friendListView;
    private static ListView slopeListView;

    private ArrayList<String> friends;
    public static String username;
    static UserActivity context;
    private EditText editFriendText;
    private JSONArray jsonFriends;
    private static boolean noFavorites;
    private static boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        friends = new ArrayList<String>();
        noFavorites = false;
        editFriendText = (EditText) findViewById(R.id.friendText);
        username = getIntent().getExtras().getString("username");
        TextView userHeader = (TextView) findViewById(R.id.nameText);
        if (username == null) {
            loggedIn = false;
            userHeader.setText("You have not created an account");
            return;
        }
        else {
            userHeader.setText(username);
            loggedIn = true;
        }
        context = this;
        friendListView = (ListView) findViewById(R.id.friendList);
        slopeListView = (ListView) findViewById(R.id.slopeList);


        ParseQuery friendQuery = new ParseQuery("accounts");
        friendQuery.whereEqualTo("username", username);
        friendQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                JSONArray jsonArr = object.getJSONArray("friends");
                jsonFriends = jsonArr;
                if (jsonArr == null) {
                    jsonArr = new JSONArray();
                }
                String[] arr = new String[jsonArr.length()];
                for (int i = 0; i < arr.length; i++) {
                    try {
                        final String friend = jsonArr.getJSONObject(i).getString("name");
                        arr[i] = friend;
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                if (arr.length == 0) {
                    arr = new String[1];
                    arr[0] = "No friends";
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, arr);

                // Assign adapter to ListView
                friendListView.setAdapter(adapter);

                // favorite slopes :

                JSONArray jsonSlopeArr = object.getJSONArray("favoriteSlopes");
                if (jsonSlopeArr == null) {
                    jsonSlopeArr = new JSONArray();
                }
                String[] slopeArr = new String[jsonSlopeArr.length()];
                for (int i = 0; i < slopeArr.length; i++) {
                    try {
                        final String friend = jsonSlopeArr.getJSONObject(i).getString("name");
                        slopeArr[i] = friend;
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                if (slopeArr.length == 0) {
                    noFavorites = true;;
                    slopeArr = new String[1];
                    slopeArr[0] = "No favorite slopes";
                }
                ArrayAdapter<String> slopeAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, slopeArr);

                // Assign adapter to ListView
                slopeListView.setAdapter(slopeAdapter);

                if (!noFavorites) {
                    slopeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (!noFavorites) {
                                String currentSlope = (String) adapterView.getItemAtPosition(i);
                                Intent intent = new Intent(context, ConditionDataStore.class);
                                intent.putExtra("slopeName", currentSlope);
                                intent.putExtra("username", username);
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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

    public void removeFriend(View view) {
        if (!loggedIn) {
            return;
        }
        final String name = editFriendText.getText().toString();
        JSONArray newJsonFriends = new JSONArray();
        if (name != null) {
            if (friends.contains(name)) {
                friends.remove(name);
            }
            for (int i = 0; i < jsonFriends.length(); i++) {
                try {
                    if (!jsonFriends.getJSONObject(i).getString("name").equalsIgnoreCase(name)) {
                        newJsonFriends.put(jsonFriends.getJSONObject(i));
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
            jsonFriends = newJsonFriends;
            updateFriendData();
        }
    }

    public void addNewFriend(View view) {
        if (!loggedIn) {
            return;
        }
        String name = editFriendText.getText().toString();

        if (name != null) {
            boolean contains = false;
            for (int i = 0; i < jsonFriends.length(); i++) {
                try {
                    if (jsonFriends.getJSONObject(i).getString("name").equalsIgnoreCase(name)) {
                        contains = true;
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
            if (!contains) {
                JSONObject newFriend = new JSONObject();
                try {
                    newFriend.put("name", name);
                    jsonFriends.put(newFriend);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                updateFriendData();
            }

        }

    }

    public void updateFriendData(){
        if (!loggedIn) {
            return;
        }
        ParseQuery friendQuery = new ParseQuery("accounts");
        friendQuery.whereEqualTo("username", username);
        friendQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.put("friends", jsonFriends);
                object.saveInBackground();
//                JSONArray jsonArr = object.getJSONArray("friends");
                JSONArray jsonArr = jsonFriends;
                if (jsonArr == null) {
                    jsonArr = new JSONArray();
                }
                String[] arr = new String[jsonArr.length()];
                for (int i = 0; i < arr.length; i++) {
                    try {
                        final String friend = jsonArr.getJSONObject(i).getString("name");
                        friends.add(friend);
                        arr[i] = friend;
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                if (arr.length == 0) {
                    arr = new String[1];
                    arr[0] = "No friends";
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, arr);

                // Assign adapter to ListView
                friendListView.setAdapter(adapter);
//                friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        currentSlope = (String) adapterView.getItemAtPosition(i);
//                        showSlopeData(view);
//                    }

            }
        });
    }

    public static void updateFavoriteSlopeData() {
        if (!loggedIn) {
            return;
        }

        ParseQuery slopeQuery = new ParseQuery("accounts");
        slopeQuery.whereEqualTo("username", username);
        slopeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                JSONArray jsonSlopeArr = object.getJSONArray("favoriteSlopes");
                if (jsonSlopeArr == null) {
                    jsonSlopeArr = new JSONArray();
                }
                String[] slopeArr = new String[jsonSlopeArr.length()];
                for (int i = 0; i < slopeArr.length; i++) {
                    try {
                        final String friend = jsonSlopeArr.getJSONObject(i).getString("name");
                        slopeArr[i] = friend;
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                noFavorites = false;
                if (slopeArr.length == 0) {
                    System.out.println("no favorites");
                    noFavorites = true;
                    slopeArr = new String[1];
                    slopeArr[0] = "No favorite slopes";
                }
                ArrayAdapter<String> slopeAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, slopeArr);

                // Assign adapter to ListView
                slopeListView.setAdapter(slopeAdapter);
                if (!noFavorites) {
                    slopeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (!noFavorites) {
                                String currentSlope = (String) adapterView.getItemAtPosition(i);
                                Intent intent = new Intent(context, ConditionDataStore.class);
                                intent.putExtra("slopeName", currentSlope);
                                intent.putExtra("username", username);
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        updateFavoriteSlopeData();
    }
}

