package com.example.uts_3022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uts_3022.Utils.FetchImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class MovieViewActivity extends AppCompatActivity {
    private TextView title, release, description, language, budget, revenue;
    private ImageView poster;
    private RecyclerView genreRecycle;
    private GenreAdapter genreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String movieId = intent.getStringExtra(MainActivity.MOVIE_DATA);
        MovieViewActivity ctx = this;

        MovieDBAPI.fetchSingleMovies(this, movieId, new RequestCallback() {
            @Override
            public void onSuccess(JSONArray movielist) {
                ctx.renderLayout(movielist);
            }
        });
    }

    void renderLayout(JSONArray movies) {
        setContentView(R.layout.view_movie);

        genreRecycle = findViewById(R.id.movie_view_genre);

        title = findViewById(R.id.movie_view_title);
        release = findViewById(R.id.movie_view_release);
        description = findViewById(R.id.movie_view_description);
        language = findViewById(R.id.movie_view_language);
        budget = findViewById(R.id.movie_view_budget);
        revenue = findViewById(R.id.movie_view_revenue);
        poster = findViewById(R.id.movie_view_poster);

        try {
            JSONObject movie = movies.getJSONObject(0);
            Locale l = Locale.getDefault();
            NumberFormat fmt = NumberFormat.getNumberInstance(l);

            genreAdapter = new GenreAdapter(this, movie.getJSONArray("genres"));
            genreRecycle.setAdapter(genreAdapter);
            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            genreRecycle.setLayoutManager(horizontalLayoutManagaer);

            title.setText( movie.getString("title"));
            release.setText(": " + movie.getString("release_date"));
            description.setText( movie.getString("overview"));
            language.setText(": " + movie.getString("original_language"));
            budget.setText(": " + fmt.format(Integer.parseInt(movie.getString("budget"))) + " USD");
            revenue.setText(": " + fmt.format(Integer.parseInt(movie.getString("revenue"))) + " USD");
            String imageUrl = "https://image.tmdb.org/t/p/w300" + movie.getString("poster_path");

            Picasso.get().load(imageUrl).into(poster);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
