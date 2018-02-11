package org.app.zeroresearchtravel.androidhackdataapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.TextView;

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
        sharedPreferences = getSharedPreferences("Calendar", Context.MODE_PRIVATE);
        try {
            getApiData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getApiData() throws IOException {
        int age = sharedPreferences.getInt("age", 0);
        int leaves = sharedPreferences.getInt("leaves", 0);
        //TODO: ADD BUDGET, GENDER, AND ALTER URL
        String spec = "https://api.havail.sabre.com/v1/lists/top/destinations?origin=&lookbackweeks=&topdestinations=1";
        URL url = new URL(spec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if(conn.getResponseCode()!=200)//indicates server response is OK
            throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String output;
        StringBuilder sb = new StringBuilder();
        while((output = br.readLine())!=null) {
            sb.append(output);
        }
        Gson gson = new Gson();
        SuggestionData suggestionData = gson.fromJson(sb.toString(), SuggestionData.class);
    }
}
