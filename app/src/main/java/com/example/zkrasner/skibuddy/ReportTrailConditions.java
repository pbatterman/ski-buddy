package com.example.zkrasner.skibuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;


public class ReportTrailConditions extends ActionBarActivity {
    public static String[] conditions = {"Icy", "Powder", "Groomed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_trail_conditions);

        Spinner spinner = (Spinner) findViewById(R.id.trail_select_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kill_runs, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
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
