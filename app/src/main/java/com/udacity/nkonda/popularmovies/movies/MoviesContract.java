package com.udacity.nkonda.popularmovies.movies;

import com.udacity.nkonda.popularmovies.BasePresenter;
import com.udacity.nkonda.popularmovies.BaseState;
import com.udacity.nkonda.popularmovies.BaseView;
import com.udacity.nkonda.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public interface MoviesContract {
    interface View extends BaseView{

        void showProgress();

        void hideProgress();

        void showResults(List<Movie> popularMovies, int totalPages);

        void showMovieDetails(int movieId);

        void showError(String errorMsg);
    }

    interface Presenter extends BasePresenter<State> {

        void load();

        void onMovieSelected(int position);

        void onSortOrderChanged(SortOrder sortOrder);

        void onScrolledToTop();

        void onScrolledToBottom();
    }

    interface State extends BaseState {

        int getLastPageNumber();

        SortOrder getLastSortOrder();
    }
}
