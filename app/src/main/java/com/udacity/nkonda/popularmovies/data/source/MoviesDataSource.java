package com.udacity.nkonda.popularmovies.data.source;

import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.constants.SortOrder;
import com.udacity.nkonda.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public interface MoviesDataSource {

    interface LoadMoviesCallback {

        void onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {

        void onMovieLoaded(Movie movie);

        void onDataNotAvailable();
    }

    void getMovies(SortOrder sortOrder, @NonNull LoadMoviesCallback callback);

    void getMovie(@NonNull int movieId, @NonNull GetMovieCallback callback);
}
