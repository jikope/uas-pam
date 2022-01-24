package com.example.uts_3022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.example.uts_3022.Utils.SQLiteManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView movieRecycle;
    private MovieAdapter movieAdapter;
    private SQLiteManager sqLiteManager;
    private ProgressBar progressBar;
    private SharedPreferences preferences;

    public static final String MOVIE_DATA = "";

    private String _API_URL = "https://api.themoviedb.org/3";
    private String _API_KEY = "?api_key=adc2a543db79411832e2221a4f2fdd61";
    private String _API_LANG = "&language=" + Locale.getDefault().getLanguage();

    MainActivity mainCtx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        movieRecycle = findViewById(R.id.movie_recycle);

        MainActivity mainCtx = this;

        movieAdapter = new MovieAdapter(this, new JSONArray());
        movieRecycle.setAdapter(movieAdapter);
        movieRecycle.setLayoutManager(new LinearLayoutManager(this));

        sqLiteManager = new SQLiteManager(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        JSONArray movies = sqLiteManager.getData();
        Log.d("MAIN", movies.toString());

        long cacheTime = preferences.getLong("cache", 0);
        boolean isCacheExpired = false;

        if (cacheTime > 0) {
            long currentTime = new Date().getTime();
            long diffTime = (currentTime - cacheTime) / 1000;

            if (diffTime > 60 * 10){ // 10 Minutes cache expired time
                isCacheExpired = true;
            }
        }

        // Wait request to finish with passing callback function
        if (movies.length() > 0 && !isCacheExpired) {
            this.showData(movies);
        } else {
            MovieDBAPI.fetchMovies(this, new RequestCallback() {
                @Override
                public void onSuccess(JSONArray movielist) {
                                                                 mainCtx.renderLayout(movielist);
                                                                                                 }
            });
        }
    }

    void renderLayout(JSONArray movieList) {
        sqLiteManager.deleteOldCache();
        JSONArray newData = new JSONArray();

        for (int i = 0; i < movieList.length(); i++) {
            try {
                JSONObject movie = movieList.getJSONObject(i);
                if (movie.getString("title").isEmpty()) {
                    continue;
                } else {
                    sqLiteManager.addData(movie);
                    newData.put(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.showData(newData);
    }

    private void showData(JSONArray data) {
        movieAdapter.setFilter(data);
        progressBar.setVisibility(View.GONE);
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onMenuClicked(MenuItem item) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }



}