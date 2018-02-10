package org.app.zeroresearchtravel.androidhackdataapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class InputActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ArrayList<String> eventStringList;
    private ArrayList<EventDataCustom> eventDataCustomArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        //populating Spinner data
        Spinner genderSpinner = (Spinner)findViewById(R.id.gender_entry);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the genderSpinner
        genderSpinner.setAdapter(arrayAdapter);
        genderSpinner.setSelection(0);
        Spinner budgetSpinner = (Spinner)findViewById(R.id.budget_entry);
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.budget_array, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(arrayAdapter);
        budgetSpinner.setSelection(0);

        //getting shared prefs
        sharedPreferences = getSharedPreferences("calendar", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        breakStringData(sharedPreferences.getString("string", null));
    }

    private void breakStringData(String string) {
        //first breaking into individual events
        StringTokenizer stringTokenizer = new StringTokenizer(string, "|||");
        while(stringTokenizer.hasMoreTokens()) {
            eventStringList.add(stringTokenizer.nextToken());
        }
    }
    
    private void breakEventData() {
        for(String string: eventStringList) {
            StringTokenizer stringTokenizer = new StringTokenizer(string, "|");
            String summary = stringTokenizer.nextToken();
            String startDate = stringTokenizer.nextToken();
            String endDate = stringTokenizer.nextToken();
            String diffDays = stringTokenizer.nextToken();
            EventDataCustom eventDataCustom = new EventDataCustom(summary, startDate, endDate, diffDays);
            eventDataCustomArrayList.add(eventDataCustom);
        }
    }
}
