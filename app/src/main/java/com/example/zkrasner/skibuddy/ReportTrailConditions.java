package com.example.zkrasner.skibuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class ReportTrailConditions extends ActionBarActivity {
    public static String[] conditions = {"Icy","Granular", "Groomed", "Packed Powder", "Powder"};
    public String mountain;
    ArrayList<String> trailNames = new ArrayList<String>();
    int selectedItemIndex = 0;
    private Spinner conditionSelectionSpinner;
    private ReportTrailConditions context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mountain = getIntent().getExtras().getString("mtnName");
        setContentView(R.layout.activity_report_trail_conditions);
        context = this;

        // get all trails from mountain in intent
        final Spinner trailSelectedSpinner = null;

        ParseQuery pq = new ParseQuery("Mountain");
        pq.whereEqualTo("name", mountain);
        pq.getFirstInBackground(new GetCallback<ParseObject>() {
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
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

                // display trails from specific mountain
                final Spinner trailSelectedSpinner = (Spinner) findViewById(R.id.trail_select_spinner);
                ArrayAdapter<String> trailNamesAdapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item,trailNames);


                // Specify the layout to use when the list of choices appears
                trailNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner

                trailSelectedSpinner.setAdapter(trailNamesAdapter);
                trailSelectedSpinner.setSelection(0);

                trailSelectedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        selectedItemIndex = trailSelectedSpinner.getSelectedItemPosition();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedItemIndex = 0;
                    }
                });
            }
        });


        conditionSelectionSpinner = (Spinner) findViewById(R.id.condition_select_spinner);
        ArrayAdapter<CharSequence> conditionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.conditions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        conditionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        conditionSelectionSpinner.setAdapter(conditionsAdapter);


        NumberPicker np = (NumberPicker) findViewById(R.id.rating_picker);
        np.setMinValue(1);
        np.setMaxValue(10);
        np.setValue(5);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_trail_conditions, menu);
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


    public void onSubmitButtonClick(View view){
        Spinner trailSpinner = (Spinner) findViewById(R.id.trail_select_spinner);
        String curr_trail = trailNames.get(selectedItemIndex);

        Spinner conditionSpinner  = (Spinner) findViewById(R.id.condition_select_spinner);
        final int curr_cond = conditionSpinner.getSelectedItemPosition();


        NumberPicker np = (NumberPicker) findViewById(R.id.rating_picker);
        final int rating = np.getValue();


        ParseQuery pq = new ParseQuery("trail");
        pq.whereEqualTo("name", curr_trail);


        pq.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                Integer numEntries = (Integer) object.getInt("numEntries");
                Double oldRating = (Double) object.getDouble("rating");
                Double newRating = ((oldRating * numEntries) + rating) / (numEntries + 1);
                object.put("numEntries", numEntries + 1);
                object.put("conditionRating", curr_cond);
                object.put("rating", newRating);
                object.saveInBackground();


            }
        });

    }
}
