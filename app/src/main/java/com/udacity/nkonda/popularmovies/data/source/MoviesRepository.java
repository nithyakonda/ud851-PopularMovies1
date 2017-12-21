package com.udacity.nkonda.popularmovies.data.source;

import static android.support.v4.util.Preconditions.checkNotNull;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.BuildConfig;
import com.udacity.nkonda.popularmovies.constants.SortOrder;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;

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
 * Created by nkonda on 12/18/17.
 */

public class MoviesRepository implements MoviesDataSource {
    private static final String TAG = "PM_MoviesRepository";

    private final static String API_KEY = BuildConfig.API_KEY;

    private final static String TMDB_BASE_MOVIES_URL = "https://api.themoviedb.org/3/movie";
    private final static String TMDB_POPULAR_MOVIES_URL = TMDB_BASE_MOVIES_URL + "/popular";
    private final static String TMDB_TOP_RATED_MOVIES_URL = TMDB_BASE_MOVIES_URL + "/top_rated";

    private static String PARAM_API_KEY = "api_key";

    private static MoviesRepository INSTANCE;

    private MoviesRepository() {
    }

    public static MoviesRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository();
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(SortOrder sortOrder, @NonNull final GetMoviesCallback callback) {
        URL getMoviesUrl = getUrl(sortOrder);
        new HttpRequestTask(new OnHttpResponseListener() {
            @Override
            public void onReceive(String httpResponse) {
                if (httpResponse == null) {
                    callback.onDataNotAvailable();
                } else {
                    List<Movie> movies = parseMoviesListJson(httpResponse);
                    if (movies.isEmpty()) {
                        callback.onDataNotAvailable();
                    } else {
                        callback.onMoviesLoaded(movies);
                    }
                }
            }
        }).execute(getMoviesUrl);
    }

    @Override
    public void getMovieDetails(@NonNull int movieId, @NonNull final GetMovieDetailsCallback callback) {
        URL getMovieDetailsUrl = getUrl(movieId);
        new HttpRequestTask(new OnHttpResponseListener() {
            @Override
            public void onReceive(String httpResponse) {
                if (httpResponse == null) {
                    callback.onDataNotAvailable();
                } else {
                    MovieDetails movieDetails = parseMovieDetailsJson(httpResponse);
                    if (movieDetails == null) {
                        callback.onDataNotAvailable();
                    } else {
                        callback.onMovieDetailsLoaded(movieDetails);
                    }
                }
            }
        }).execute(getMovieDetailsUrl);
    }

    // Helper methods
    // TODO: 12/18/17 move helper methods to appropriate utils

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

    private URL getUrl (int movieId) {
        String getMovieDetailsUrl = TMDB_BASE_MOVIES_URL + "/" + String.valueOf(movieId);
        Uri uri = Uri.parse(getMovieDetailsUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();
        return convertUriToUrl(uri);
    }

    private URL getUrl(SortOrder sortOrder) {
        Uri uri = null;
        switch (sortOrder) {
            case Popular:
                uri = Uri.parse(TMDB_POPULAR_MOVIES_URL).buildUpon()
                        .appendQueryParameter(PARAM_API_KEY, API_KEY)
                        .build();
                break;
            case TopRated:
                uri = Uri.parse(TMDB_TOP_RATED_MOVIES_URL).buildUpon()
                        .appendQueryParameter(PARAM_API_KEY, API_KEY)
                        .build();
                break;
        }
        return convertUriToUrl(uri);
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

    private List<Movie> parseMoviesListJson(String moviesListJsonStr) {
        // TODO: 12/18/17 create string variables
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray moviesJson = new JSONObject(moviesListJsonStr).getJSONArray("results");
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

    private MovieDetails parseMovieDetailsJson(String movieDetailsJsonStr) {
        MovieDetails movieDetails = null;
        try {
            JSONObject movieDetailsJson = new JSONObject(movieDetailsJsonStr);
            movieDetails = new MovieDetails(
                    movieDetailsJson.getString("original_title"),
                    movieDetailsJson.getString("poster_path"),
                    movieDetailsJson.getString("overview"),
                    movieDetailsJson.getDouble("vote_average"),
                    movieDetailsJson.getString("release_date")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDetails;
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
                httpResponse = NetworkHelper.getResponseFromHttpUrl(searchUrl);
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

    interface OnHttpResponseListener {
        void onReceive(String httpResponse);
    }
}
