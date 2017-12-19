package com.udacity.nkonda.popularmovies.callbacks;

import com.udacity.nkonda.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public interface GetPopularMoviesCallback {
    public void onSuccess(List<Movie> popularMovies);
    public void onError(int errorCode);
}
