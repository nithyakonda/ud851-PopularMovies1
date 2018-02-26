package com.udacity.nkonda.popularmovies.moviedetails;

import android.net.Uri;

import com.udacity.nkonda.popularmovies.BasePresenter;
import com.udacity.nkonda.popularmovies.BaseView;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.Review;
import com.udacity.nkonda.popularmovies.data.Trailer;

import java.util.List;

/**
 * Created by nkonda on 12/21/17.
 */

public interface MovieDetailsContract {
    interface View extends BaseView{

        void showProgress();

        void hideProgress();

        void showError(String errorMsg);

        void showMovieDetails(MovieDetails movieDetails);

        void showTrailers(List<Trailer> trailers);

        void showReviews(List<Review> reviews, int currentPage, int totalPages);

        void playTrailer(Uri trailerUri);
    }

    interface Presenter extends BasePresenter {

        void load(int movieId);

        void loadTrailers(int movieId);

        void loadReviews(int movieId);

        void onTrailerSelected(int position);

        void onNextButtonClicked();

        void onBackButtonClicked();
    }
}
