package com.example.uts_3022.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class FetchImage extends AsyncTask<String, Void, Bitmap> {
    ImageView posterView;

    public FetchImage(ImageView poster) {
        this.posterView = poster;

    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap image = null;

        try {
            InputStream in = new java.net.URL(urls[0]).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch(Exception e) {
            Log.e("Error fetching image", e.toString());
        }

        return image;
    }

    protected void onPostExecute(Bitmap poster) {
        this.posterView.setImageBitmap(poster);
    }
}
