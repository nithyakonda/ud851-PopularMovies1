package com.udacity.nkonda.popularmovies.utils;

import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nkonda on 12/22/17.
 */

public class JsonHelper {
    private static final String JSONKEY_RESULTS = "results";
    private static final String JSONKEY_ID = "id";
    private static final String JSONKEY_TOTAL_PAGES = "total_pages";
    private static final String JSONKEY_TITLE = "title";
    private static final String JSONKEY_POSTER_PATH = "poster_path";
    private static final String JSONKEY_ORIGINAL_TITLE = "original_title";
    private static final String JSONKEY_OVERVIEW = "overview";
    private static final String JSONKEY_VOTE_AVERAGE = "vote_average";
    private static final String JSONKEY_RELEASE_DATE = "release_date";

    public static int getTotalPages(String moviesListJsonStr) {
        int totalPages = 0;
        try {
            JSONObject responseJson = new JSONObject(moviesListJsonStr);
            totalPages = responseJson.getInt(JSONKEY_TOTAL_PAGES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return totalPages;
    }

    public static List<Movie> parseMoviesListJson(String moviesListJsonStr) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray moviesJson = new JSONObject(moviesListJsonStr).getJSONArray(JSONKEY_RESULTS);
            JSONObject movieJson;
            int id;
            String title;
            String posterPath;
            for (int i = 0; i < moviesJson.length(); i++) {
                movieJson = moviesJson.getJSONObject(i);
                id = movieJson.getInt(JSONKEY_ID);
                title = movieJson.getString(JSONKEY_TITLE);
                posterPath = movieJson.getString(JSONKEY_POSTER_PATH);
                movies.add(new Movie(id, title, posterPath));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static MovieDetails parseMovieDetailsJson(String movieDetailsJsonStr) {
        MovieDetails movieDetails = null;
        try {
            JSONObject movieDetailsJson = new JSONObject(movieDetailsJsonStr);
            movieDetails = new MovieDetails(
                    movieDetailsJson.getString(JSONKEY_ORIGINAL_TITLE),
                    movieDetailsJson.getString(JSONKEY_POSTER_PATH),
                    movieDetailsJson.getString(JSONKEY_OVERVIEW),
                    movieDetailsJson.getDouble(JSONKEY_VOTE_AVERAGE),
                    formatDate(movieDetailsJson.getString(JSONKEY_RELEASE_DATE))
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDetails;
    }

    public static String formatDate(String dateStr) {
        String formattedStr = null;
        try {
            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(dateStr);
            formattedStr = new SimpleDateFormat("d MMMM yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedStr;
    }
}
