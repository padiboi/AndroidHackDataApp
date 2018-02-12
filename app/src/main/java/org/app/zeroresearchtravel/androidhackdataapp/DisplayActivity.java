package org.app.zeroresearchtravel.androidhackdataapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class DisplayActivity extends AppCompatActivity {
    private CardView cardView;
    private TextView textView;
    private ArrayList<SuggestionData> suggestionData;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private String[] eventArray;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> eventStringList = new ArrayList<>();
    TextView apiResponseText;
    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        sharedPreferences = getApplicationContext().getSharedPreferences("calendar", MODE_PRIVATE);
        try {
            getApiData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cData = sharedPreferences.getString("calstring", null);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        apiResponseText = (TextView) findViewById(R.id.response);
        if(cData!=null) {
            breakStringData(cData);
            eventArray = eventStringList.toArray(new String[eventStringList.size()]);
            recyclerAdapter = new RecyclerAdapter(eventArray);
        }
        Log.i("kinks", "null");
        recyclerView.setAdapter(recyclerAdapter);
        /*if(cData!=null) {
            Log.i("kinks", cData);
            ..TextView calen = (TextView) findViewById(R.id.first_suggestion_card);
            calen.setText(cData);
        }*/
    }

    private void breakStringData(String string) {
        //first breaking into individual events
        if(string!=null) {
            StringTokenizer stringTokenizer = new StringTokenizer(string, "\n");
            while(stringTokenizer.hasMoreTokens()) {
                eventStringList.add(stringTokenizer.nextToken());
            }
        }
    }

    private void getApiData() throws IOException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://app.blisteringly74.hasura-app.io/predict?";

        int age = sharedPreferences.getInt("age", 23);
        int leaves = sharedPreferences.getInt("leaves", 0);
        int budget = sharedPreferences.getInt("selectedBudgetItem", 3);
        int gender = sharedPreferences.getInt("selectedGenderItem", 1);
        int quarter = 3;

        String finalURL = url + "age=" + age + "&sex=" + getResources().getStringArray(R.array.gender_array)[gender]
                + "&budget=" + 300000 /*getResources().getStringArray(R.array.budget_array)[budget]*/ + "&quarter=" + quarter +
                "&duration=" + 9;

                Log.i("Kinks", finalURL);

        //TODO: ADD BUDGET, GENDER, AND ALTER URL

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        try {
                            apiResponseText.setText(response);
                            JSONObject json = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
