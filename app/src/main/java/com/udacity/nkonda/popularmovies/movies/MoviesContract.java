package com.udacity.nkonda.popularmovies.movies;

import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.BasePresenter;
import com.udacity.nkonda.popularmovies.BaseView;
import com.udacity.nkonda.popularmovies.data.Movie;

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
    }

    interface Presenter extends BasePresenter {

        void load();

        void onScrolledToEnd();

        void onMovieSelected();
    }
}
