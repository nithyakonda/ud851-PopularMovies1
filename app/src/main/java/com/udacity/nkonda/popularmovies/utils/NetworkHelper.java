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

import com.udacity.nkonda.popularmovies.BuildConfig;
import com.udacity.nkonda.popularmovies.movies.SortOrder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkHelper {
    private final static String TAG = "PM_NetworkHelper";

    private final static String API_KEY = BuildConfig.API_KEY;

    private final static String TMDB_BASE_MOVIES_URL = "https://api.themoviedb.org/3/movie";
    private final static String TMDB_POPULAR_MOVIES_URL = TMDB_BASE_MOVIES_URL + "/popular";
    private final static String TMDB_TOP_RATED_MOVIES_URL = TMDB_BASE_MOVIES_URL + "/top_rated";
    private final static String TMDB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private final static String TMDB_TRAILERS_URL = TMDB_BASE_MOVIES_URL + "/%d/videos";
    private final static String TMDB_REVIEWS_URL = TMDB_BASE_MOVIES_URL + "/%d/reviews";
    private final static String YOUTUBE_BASE_URL = "http://www.youtube.com/watch";

    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_PAGE = "page";
    private final static String PARAM_VIDEO_ID = "v";
    private static NetworkHelper INSTANCE;

    private NetworkHelper() {}

    public static NetworkHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkHelper();
        }
        return INSTANCE;
    }

    public void startHttpRequestTask(URL url, OnHttpResponseListener listener) {
        new HttpRequestTask(listener).execute(url);
    }

    private Uri getUriWithApiKey(String url) {
        return Uri.parse(url).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();
    }

    private URL appendApiKeyParamTo(String url) {
        return convertUriToUrl(getUriWithApiKey(url));
    }

    public URL getMovieDetailsUrl(int movieId) {
        String getMovieDetailsUrl = TMDB_BASE_MOVIES_URL + "/" + String.valueOf(movieId);
        Uri uri = getUriWithApiKey(getMovieDetailsUrl);
        return convertUriToUrl(uri);
    }

    public URL getUrl(SortOrder sortOrder, int page) {
        Uri uri = null;
        String baseUrl = null;
        switch (sortOrder) {
            case Popular:
                baseUrl = TMDB_POPULAR_MOVIES_URL;
                break;
            case TopRated:
                baseUrl = TMDB_TOP_RATED_MOVIES_URL;
                break;
        }
        uri = getUriWithApiKey(baseUrl).buildUpon()
                .appendQueryParameter(PARAM_PAGE, String.valueOf(page))
                .build();
        return convertUriToUrl(uri);
    }

    public String getUrl(String imagePath) {
        return TMDB_BASE_IMAGE_URL + imagePath;
    }

    public URL getTrailersUrl(int movieId) {
        String trailersUrl = String.format(TMDB_TRAILERS_URL, movieId);
        return appendApiKeyParamTo(trailersUrl);
    }

    public URL getReviewsUrl(int movieId) {
        String reviewsUrl = String.format(TMDB_REVIEWS_URL, movieId);
        return appendApiKeyParamTo(reviewsUrl);
    }

    public Uri getYoutubeVideoUri(String videoId) {
        Uri uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_VIDEO_ID, videoId)
                .build();
        return uri;
    }

    private URL convertUriToUrl(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
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

    // Async Task
    private class HttpRequestTask extends AsyncTask<URL, Void, String> {
        final OnHttpResponseListener mListener;

        public HttpRequestTask(OnHttpResponseListener listener) {
            mListener = listener;
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String httpResponse = null;
            try {
                httpResponse = getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResponse;
        }

        @Override
        protected void onPostExecute(String httpResponse) {
            if (mListener != null) {
                mListener.onReceive(httpResponse);
            }
        }
    }

    public interface OnHttpResponseListener {
        void onReceive(String httpResponse);
    }
}