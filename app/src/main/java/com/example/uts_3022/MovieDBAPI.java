package com.example.uts_3022;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MovieDBAPI {

    private static String _API_URL = "https://api.themoviedb.org/3";
    private static String _API_KEY = "?api_key=adc2a543db79411832e2221a4f2fdd61";
    private static String _API_LANG = "&language=" + MovieDBAPI.getLanguage();

    public static String getLanguage() {
        String lang = Locale.getDefault().getLanguage();
        if (lang == "in") {
            lang = "id";
        }
        return lang;
    }

    public static void fetchMovies(Context ctx, final RequestCallback callback) {
        String url = MovieDBAPI._API_URL + "/trending/all/week" + MovieDBAPI._API_KEY + MovieDBAPI._API_LANG;
        RequestQueue reqQueue = Volley.newRequestQueue(ctx);

        JSONObject responseBody = new JSONObject();
        JSONArray movies = new JSONArray();

        StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);

                    callback.onSuccess(res.getJSONArray("results"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                Log.d("Error respnse", err.toString());
            }
        });
        stringReq.setShouldCache(false);
        reqQueue.add(stringReq);
    }

    public static void fetchSingleMovies(Context ctx, String movieId, final RequestCallback callback) {
        String url = MovieDBAPI._API_URL + "/movie/" + movieId  + MovieDBAPI._API_KEY + MovieDBAPI._API_LANG;
        RequestQueue reqQueue = Volley.newRequestQueue(ctx);

        JSONObject responseBody = new JSONObject();
        JSONArray movies = new JSONArray();

        StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    JSONArray movies = new JSONArray();
                    movies.put(0, res);

                    callback.onSuccess(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                Log.d("Error respnse", err.toString());
            }
        });

        reqQueue.add(stringReq);
    }
}
