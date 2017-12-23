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
    public static List<Movie> parseMoviesListJson(String moviesListJsonStr) {
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

    public static MovieDetails parseMovieDetailsJson(String movieDetailsJsonStr) {
        MovieDetails movieDetails = null;
        try {
            JSONObject movieDetailsJson = new JSONObject(movieDetailsJsonStr);
            movieDetails = new MovieDetails(
                    movieDetailsJson.getString("original_title"),
                    movieDetailsJson.getString("poster_path"),
                    movieDetailsJson.getString("overview"),
                    movieDetailsJson.getDouble("vote_average"),
                    formatDate(movieDetailsJson.getString("release_date"))
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
