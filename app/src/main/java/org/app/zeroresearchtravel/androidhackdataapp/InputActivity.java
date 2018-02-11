package org.app.zeroresearchtravel.androidhackdataapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class InputActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ArrayList<String> eventStringList;
    private ArrayList<EventDataCustom> eventDataCustomArrayList;
    private EditText ageText;
    private EditText leaveText;
    private Spinner genderSpinner;
    private Spinner budgetSpinner;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        //getting shared prefs
        sharedPreferences = getSharedPreferences("calendar", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean("dataInputDone", false)) {
            Intent intent = new Intent(this, DisplayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        ageText = (EditText)findViewById(R.id.age_entry);
        leaveText = (EditText)findViewById(R.id.leaves_entry);
        //populating Spinner data
        genderSpinner = (Spinner)findViewById(R.id.gender_entry);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the genderSpinner
        genderSpinner.setAdapter(arrayAdapter);
        genderSpinner.setSelection(0);
        budgetSpinner = (Spinner)findViewById(R.id.budget_entry);
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.budget_array, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(arrayAdapter);
        budgetSpinner.setSelection(0);
        breakStringData(sharedPreferences.getString("string", null));
        //event data obtained, adding user info
        Button acceptButton = (Button)findViewById(R.id.accepted_button);
        if(storeUserInfo()) {
            editor.putBoolean("dataInputDone", true).commit();
            acceptButton.setVisibility(View.VISIBLE);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(InputActivity.this, DisplayActivity.class);
                    startActivity(intent);
                }
            });
        }
        else {
            Toast toast = Toast.makeText(this, "Enter all details correctly", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean storeUserInfo() {
        int age = Integer.parseInt(ageText.getText().toString());
        int leaves = Integer.parseInt(leaveText.getText().toString());
        editor.putInt("age", age);
        if(age <= 0)
            return false;
        editor.putInt("leaves", leaves);
        if(leaves > 365)
            return false;
        final int[] selectedGenderItem = new int[1];
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGenderItem[0] = adapterView.getSelectedItemPosition();
                editor.putInt("selectedGenderItem", selectedGenderItem[0]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        if(selectedGenderItem[0] == 0)
            return false;
        final int[] selectedBudgetItem = new int[1];
        budgetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBudgetItem[0] = adapterView.getSelectedItemPosition();
                editor.putInt("selectedBudgetItem", selectedBudgetItem[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        if(selectedBudgetItem[0] == 0)
            return false;
        editor.commit();
        CustomerInfo customerInfo = new CustomerInfo(
                sharedPreferences.getInt("age", age),
                sharedPreferences.getInt("leaves", leaves),
                sharedPreferences.getInt("selectedGenderItem", 0),
                sharedPreferences.getInt("selectedBudgetItem", 0)
        );
        return true;
    }

    private void breakStringData(String string) {
        //first breaking into individual events
        if(string!=null) {
            StringTokenizer stringTokenizer = new StringTokenizer(string, "|||");
            while(stringTokenizer.hasMoreTokens()) {
                eventStringList.add(stringTokenizer.nextToken());
            }
            breakEventData();
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
