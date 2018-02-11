package org.app.zeroresearchtravel.androidhackdataapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {
    private CardView cardView;
    private TextView textView;
    private ArrayList<SuggestionData> suggestionData;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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
        setContentView(R.layout.activity_display);
        sharedPreferences = getApplicationContext().getSharedPreferences("Calendar", Context.MODE_PRIVATE);
        try {
            getApiData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cData = sharedPreferences.getString("calstring", null);
        /*if(cData!=null) {
            Log.i("kinks", cData);
            ..TextView calen = (TextView) findViewById(R.id.first_suggestion_card);
            calen.setText(cData);
        }*/
    }

    private void getApiData() throws IOException {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://app.blisteringly74.hasura-app.io/";

        int age = sharedPreferences.getInt("age", 0);
        int leaves = sharedPreferences.getInt("leaves", 0);
        int budget = sharedPreferences.getInt("selectedBudgetItem", 0);
        int gender = sharedPreferences.getInt("selectedGenderItem", 0);

        //TODO: ADD BUDGET, GENDER, AND ALTER URL

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
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
