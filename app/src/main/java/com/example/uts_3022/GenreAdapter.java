package com.example.uts_3022;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreItem> {
    Context context;
    JSONArray genres;

    public GenreAdapter(Context ctx, JSONArray data) {
        context = ctx;
        genres = data;
    }

    @NonNull
    @Override
    public GenreItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.genre_button, parent, false);
        return new GenreItem(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.GenreItem holder, int position) {
        try {
            JSONObject movie = genres.getJSONObject(position);
            holder.genre_id = movie.getString("id");
            holder.genre_name.setText(movie.getString("name"));

            // new FetchImage(holder.image).execute("https://themoviedb.org/t/p/w500" + movie.getString("poster_path"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() { return this.genres.length(); }

    public class GenreItem extends RecyclerView.ViewHolder {
        String genre_id;
        TextView genre_name;

        public GenreItem(@NonNull View itemView, Context ctx) {
            super(itemView);

            GenreAdapter.GenreItem itemCtx = this;

            genre_name = itemView.findViewById(R.id.genre_name);
        }
    }
}
