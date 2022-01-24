package com.example.uts_3022;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uts_3022.Utils.FetchImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItem> {
    Context context;
    JSONArray movieList;

    public MovieAdapter(Context ctx, JSONArray data) {
        context = ctx;
        movieList = data;
    }

    @NonNull
    @Override
    public MovieItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_row, parent, false);
        return new MovieItem(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItem holder, int position) {
        try {
            JSONObject movie = movieList.getJSONObject(position);
            holder.title.setText(movie.getString("title"));
            holder.description.setText(movie.getString("overview"));
            holder.release.setText(movie.getString("release_date"));
            String imageUrl = "https://image.tmdb.org/t/p/w300" + movie.getString("poster_path");

            Picasso.get().load(imageUrl).into(holder.image);

            if (movie.getString("title").isEmpty()) {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                lp.setMargins(0, 10, 0, 10);
                holder.itemView.setLayoutParams(lp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.movieList.length();
    }

    public class MovieItem extends RecyclerView.ViewHolder {
        TextView title, description, release;
        ImageView image;

        public MovieItem(@NonNull View itemView, Context ctx) {
            super(itemView);
            MovieItem itemCtx = this;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, MovieViewActivity.class);
                    try {
                        intent.putExtra(MainActivity.MOVIE_DATA, movieList.getJSONObject(itemCtx.getAdapterPosition()).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ctx.startActivity(intent);
                }
            });

            title = itemView.findViewById(R.id.movie_title);
            description = itemView.findViewById(R.id.movie_description);
            release = itemView.findViewById(R.id.movie_release);
            image = itemView.findViewById(R.id.movie_poster);
        }
    }

    public void setFilter(JSONArray data) {
        this.movieList = data;
        notifyDataSetChanged();
    }
}
