package org.app.zeroresearchtravel.androidhackdataapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
    private int age, leaves, selectedGenderItem, selectedBudgetItem;
    private boolean flag1, flag2, flag3, flag4;
    private Button acceptButton;
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
        sharedPreferences = getApplicationContext().getSharedPreferences("calendar", Context.MODE_PRIVATE);
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
        budgetSpinner = (Spinner)findViewById(R.id.budget_entry);
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.budget_array, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(arrayAdapter);
        String cData = sharedPreferences.getString("calstring", null);
        if(cData!=null) {
            Log.i("kinks", cData);
            TextView calen = (TextView) findViewById(R.id.calen);
            calen.setText(cData);
        }




        //event data obtained, adding user info
        acceptButton = (Button)findViewById(R.id.accepted_button);
        acceptButton.setVisibility(View.VISIBLE);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeUserInfo();
            }
        });
    }

    private void storeUserInfo() {
        editor.putInt("age", Integer.parseInt(ageText.getText().toString()));
        editor.putInt("leaves", Integer.parseInt(leaveText.getText().toString()));
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGenderItem = adapterView.getSelectedItemPosition();
                editor.putInt("selectedGenderItem", selectedGenderItem);
                flag3 = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        budgetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBudgetItem = adapterView.getSelectedItemPosition();
                editor.putInt("selectedBudgetItem", selectedBudgetItem);
                flag4 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
            });
            Intent intent = new Intent(InputActivity.this, DisplayActivity.class);
            startActivity(intent);
    }

    private void breakStringData(String string) {
        //first breaking into individual events
        if(string!=null) {
            StringTokenizer stringTokenizer = new StringTokenizer(string, "\n");
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
