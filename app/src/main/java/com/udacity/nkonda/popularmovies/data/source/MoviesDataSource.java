package com.udacity.nkonda.popularmovies.data.source;

import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.movies.SortOrder;

import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public interface MoviesDataSource {

    interface GetMoviesCallback {

        void onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface GetMovieDetailsCallback {

        void onMovieDetailsLoaded(MovieDetails movieDetails);

        void onDataNotAvailable();
    }

    void getMovies(SortOrder sortOrder, @NonNull GetMoviesCallback callback);

    void getMovieDetails(@NonNull int movieId, @NonNull GetMovieDetailsCallback callback);
}
