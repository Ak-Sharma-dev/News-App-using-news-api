package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private Spinner spinner;
    private Spinner spinner2;
    private ImageView imageView;
    public static String choice;
    public static String choice2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Country, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<news> newsList = new ArrayList<>();
                String country_code ;
                if(choice2.equals("India")){
                    country_code = "in";
                }
                else if(choice2.equals("USA")){
                    country_code = "us";
                }
                else if(choice2.equals("Australia")){
                    country_code = "au";
                }
                else if(choice2.equals("Russia")){
                    country_code = "ru";
                }
                else if(choice2.equals("France")){
                    country_code = "fr";
                }
                else if(choice2.equals("United Kingdom")){
                    country_code = "gb";
                }
                else{
                    country_code = "in";
                }
                if(choice.equals("")){
                    choice = "general";
                }
                String url = "https://saurav.tech/NewsAPI/top-headlines/category/"+choice+"/"+country_code+".json";
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray News = response.getJSONArray("articles");
                            for(int i=0;i<News.length();i++) {
                                news first_news = new news();
                                JSONObject first_news_from_api = News.getJSONObject(i);
                                first_news.setAuthor(first_news_from_api.getString("author"));
                                first_news.setTitle(first_news_from_api.getString("title"));
                                first_news.setDescription(first_news_from_api.getString("description"));
                                first_news.setPublishedAt(first_news_from_api.getString("publishedAt"));
                                first_news.setUrl(first_news_from_api.getString("url"));
                                first_news.setUrlToImage(first_news_from_api.getString("urlToImage"));
                                newsList.add(first_news);
                            }
                            CustomAdapter ad = new CustomAdapter(newsList,MainActivity.this);
                            recyclerView.setAdapter(ad);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went Wrong!!", Toast.LENGTH_SHORT).show();
                    }
                });
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(objectRequest);
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner)adapterView;
        Spinner spinner2 = (Spinner)adapterView;
        if(spinner.getId() == R.id.spinner)
        {
            choice = adapterView.getItemAtPosition(i).toString();
        }
        if(spinner2.getId() == R.id.spinner2)
        {
            choice2 = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}