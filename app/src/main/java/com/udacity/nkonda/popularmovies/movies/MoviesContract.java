package com.udacity.nkonda.popularmovies.movies;

import com.udacity.nkonda.popularmovies.BasePresenter;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;

import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public interface MoviesContract {
    interface View {

        void showProgress();

        void hideProgress();

        void showResults(List<Movie> popularMovies);

        void showError(String errorMsg);

        void showMovieDetails(MovieDetails movieDetails);
    }

    interface Presenter extends BasePresenter {

        void load(SortOrder sortOrder);

        void onSortOrderChanged(SortOrder sortOrder);

        void onScrolledToEnd();

        void onMovieSelected(int movieId);
    }
}
