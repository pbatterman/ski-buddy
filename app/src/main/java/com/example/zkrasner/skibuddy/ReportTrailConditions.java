package com.example.zkrasner.skibuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mountain = getIntent().getExtras().getString("mtnName");
        setContentView(R.layout.activity_report_trail_conditions);

        // get all trails from mountain in intent

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

                                    }
                                });

        // display trails from specific mountain
        Spinner spinner = (Spinner) findViewById(R.id.trail_select_spinner);

        String[] a = (String[]) trailNames.toArray();

        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,a);


        

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        System.out.println("trailnames had size " + trailNames);
        spinner.setAdapter(adapter);


        Spinner spinner2 = (Spinner) findViewById(R.id.condition_select_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.conditions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);


        NumberPicker np=
                (NumberPicker) findViewById(R.id.rating_picker);
        np.setMaxValue(10);
        np.setMinValue(1);



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
        String curr_trail = trailSpinner.getSelectedItem().toString();
        System.out.println("trail was " + curr_trail);

        Spinner conditionSpinner  = (Spinner) findViewById(R.id.condition_select_spinner);
        String curr_cond = conditionSpinner.getSelectedItem().toString();

        int q = -1;
        for(int i = 0; i < conditions.length; i++){
            if(conditions[i].equals(curr_cond)){
                q = i;
            }
        }

        NumberPicker np = (NumberPicker) findViewById(R.id.rating_picker);
        int rating = np.getValue();


        TrailDataStore.InsertRating(curr_trail, q, rating);

    }
}
