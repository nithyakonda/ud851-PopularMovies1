package com.udacity.nkonda.popularmovies.moviedetails;

import com.udacity.nkonda.popularmovies.BasePresenter;
import com.udacity.nkonda.popularmovies.data.MovieDetails;

/**
 * Created by nkonda on 12/21/17.
 */

public interface MovieDetailsContract {
    interface View {

        void showProgress();

        void hideProgress();

        void showError(String errorMsg);

        void showMovieDetails(MovieDetails movieDetails);
    }

    interface Presenter extends BasePresenter {

        void load(int movieId);
    }
}
