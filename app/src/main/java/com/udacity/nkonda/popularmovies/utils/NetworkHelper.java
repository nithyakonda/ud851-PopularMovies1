/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.udacity.nkonda.popularmovies.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.udacity.nkonda.popularmovies.BuildConfig;
import com.udacity.nkonda.popularmovies.callbacks.GetPopularMoviesCallback;
import com.udacity.nkonda.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkHelper {
    private final static String TAG = "PM_NetworkHelper";

    private final static String API_KEY = BuildConfig.API_KEY;

    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/popular";

    private final static String TMDB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    private static String PARAM_API_KEY = "api_key";
    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";

    /**
     * Builds the URL used to query GitHub.
     *
     * @param githubSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the GitHub.
     */
    public static URL buildUrl() {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private List<Movie> parseMoviesListJson(String moviesListJsonStr) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray moviesJson = new JSONArray(moviesListJsonStr);
            JSONObject movieJson;
            int id;
            String title;
            String posterPath;
            for (int i = 0; i < moviesJson.length(); i++) {
                movieJson = moviesJson.getJSONObject(i);
                id = movieJson.getInt("id");
                title = movieJson.getString("title");
                posterPath = movieJson.getString("poster_path");
                movies.add(new Movie(id, title, posterPath));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    // Async Task
    private class HttpRequestTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String httpResponse = null;
            try {
                httpResponse = NetworkHelper.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResponse;
        }

        @Override
        protected void onPostExecute(String httpResponse) {
            Log.d(TAG, parseMoviesListJson(httpResponse).toString());
        }
    }

    public interface OnHttpResponseReceivedListener {
        void onReceive(String httpResponse);
    }
}