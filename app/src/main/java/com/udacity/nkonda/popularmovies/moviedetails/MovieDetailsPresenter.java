package com.udacity.nkonda.popularmovies.moviedetails;

import android.net.Uri;

import com.udacity.nkonda.popularmovies.BaseState;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.Review;
import com.udacity.nkonda.popularmovies.data.Trailer;
import com.udacity.nkonda.popularmovies.data.source.MoviesDataSource;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;

import java.util.List;

/**
 * Created by nkonda on 12/21/17.
 */

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter {

    private final MoviesRepository mRepository;

    private final MovieDetailsContract.View mView;

    private static List<Trailer> mTrailers;
    private static final int mDefaultPageNumber = 1;

    private static int mLastPageNumber = mDefaultPageNumber;
    private static int mMovieId;
    private static MovieDetails sMovieDetails;

    public MovieDetailsPresenter(MoviesRepository repository, MovieDetailsContract.View view) {
        mRepository = repository;
        mView = view;
    }

    @Override
    public void start(BaseState state) {

    }

    @Override
    public BaseState getState() {
        return null;
    }

    @Override
    public void load(int movieId) {
        mMovieId = movieId;
        if (mView.isOnline()) {
            mView.showProgress();

            mRepository.getMovieDetails(movieId, new MoviesDataSource.GetMovieDetailsCallback() {
                @Override
                public void onMovieDetailsLoaded(MovieDetails movieDetails) {
                    sMovieDetails = movieDetails;
                    mView.hideProgress();
                    mRepository.findFavoriteMovie(mMovieId, new MoviesDataSource.FindFavoriteMovieCallback() {
                        @Override
                        public void onMovieFound(boolean isFavorite) {
                            sMovieDetails.setFavorite(isFavorite);
                            mView.markAsFavorite(isFavorite);
                        }
                    });
                    mView.showMovieDetails(movieDetails);
                }

                @Override
                public void onDataNotAvailable() {
                    mView.showError("Oops! Something went wrong. Please try again later.");
                }
            });
        } else {
            mView.showError("You are not connected to the internet");
        }
    }

    @Override
    public void loadTrailers(int movieId) {
        if (mView.isOnline()) {

            mRepository.getTrailers(movieId, new MoviesDataSource.GetTrailersCallback() {
                @Override
                public void onTrailersLoaded(List<Trailer> trailers) {
                    mTrailers = trailers;
                    mView.showTrailers(trailers);
                }

                @Override
                public void onDataNotAvailable() {
                    mView.showError("Oops! Could not load trailers. Please try again later.");
                }
            });
        } else {
            mView.showError("You are not connected to the internet");
        }
    }

    @Override
    public void loadReviews(int movieId) {
        if (mView.isOnline()) {

            mRepository.getReviews(movieId, mLastPageNumber, new MoviesDataSource.GetReviewsCallback() {
                @Override
                public void onReviewsLoaded(List<Review> reviews, int totalPages) {
                    mView.showReviews(reviews, mLastPageNumber, totalPages);
                }

                @Override
                public void onDataNotAvailable() {
                    mView.showError("Oops! Could not load reviews. Please try again later.");
                }
            });
        } else {
            mView.showError("You are not connected to the internet");
        }
    }

    @Override
    public void onTrailerSelected(int position) {
        Uri trailerUri = NetworkHelper.getInstance().getYoutubeVideoUri(mTrailers.get(position).getId());
        mView.playTrailer(trailerUri);
    }

    @Override
    public void onBackButtonClicked() {
        if (mLastPageNumber > 1) {
            mLastPageNumber--;
        }
        loadReviews(mMovieId);
    }

    @Override
    public void onNextButtonClicked() {
        mLastPageNumber++;
        loadReviews(mMovieId);
    }

    @Override
    public void onFavoriteButtonClicked(int movieId) {
        if (sMovieDetails.isFavorite()) {
            mRepository.removeFavoriteMovie(movieId);
        } else {
            mRepository.addFavoriteMovie(new Movie(movieId, sMovieDetails.getOriginalTitle()));
        }
        mView.markAsFavorite(!sMovieDetails.isFavorite());
    }
}
