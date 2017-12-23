package com.udacity.nkonda.popularmovies.movies;

import com.udacity.nkonda.popularmovies.BasePresenter;
import com.udacity.nkonda.popularmovies.BaseState;
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

    interface Presenter extends BasePresenter<State> {

        void load();

        void onSortOrderChanged(SortOrder sortOrder);

        void onScrolledToEnd();
    }

    interface State extends BaseState {

        int getLastPageNumber();

        SortOrder getLastSortOrder();
    }
}
