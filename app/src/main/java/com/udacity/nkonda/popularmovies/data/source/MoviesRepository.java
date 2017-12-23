package com.udacity.nkonda.popularmovies.data.source;

import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.movies.SortOrder;
import com.udacity.nkonda.popularmovies.utils.JsonHelper;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;

import java.net.URL;
import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public class MoviesRepository implements MoviesDataSource {
    private static final String TAG = "PM_MoviesRepository";

    private static MoviesRepository INSTANCE;

    private NetworkHelper mNetworkHelper = NetworkHelper.getInstance();

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
        URL getMoviesUrl = mNetworkHelper.getUrl(sortOrder);
        mNetworkHelper.startHttpRequestTask(getMoviesUrl,
            new NetworkHelper.OnHttpResponseListener() {
                @Override
                public void onReceive(String httpResponse) {
                    if (httpResponse == null) {
                        callback.onDataNotAvailable();
                    } else {
                        List<Movie> movies = JsonHelper.parseMoviesListJson(httpResponse);
                        if (movies.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onMoviesLoaded(movies);
                        }
                    }
                }
            });
    }

    @Override
    public void getMovieDetails(@NonNull int movieId, @NonNull final GetMovieDetailsCallback callback) {
        URL getMovieDetailsUrl = mNetworkHelper.getUrl(movieId);
        mNetworkHelper.startHttpRequestTask(getMovieDetailsUrl,
            new NetworkHelper.OnHttpResponseListener() {
                @Override
                public void onReceive(String httpResponse) {
                    if (httpResponse == null) {
                        callback.onDataNotAvailable();
                    } else {
                        MovieDetails movieDetails = JsonHelper.parseMovieDetailsJson(httpResponse);
                        if (movieDetails == null) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onMovieDetailsLoaded(movieDetails);
                        }
                    }
                }
            });
    }
}
