package com.udacity.nkonda.popularmovies.moviedetails;

import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.source.MoviesDataSource;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;

/**
 * Created by nkonda on 12/21/17.
 */

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter {

    private final MoviesRepository mRepository;

    private final MovieDetailsContract.View mView;

    public MovieDetailsPresenter(MoviesRepository repository, MovieDetailsContract.View view) {
        mRepository = repository;
        mView = view;
    }

    @Override
    public void start() {
        // noop
    }

    @Override
    public void load(int movieId) {
        mView.showProgress();

        mRepository.getMovieDetails(movieId, new MoviesDataSource.GetMovieDetailsCallback() {
            @Override
            public void onMovieDetailsLoaded(MovieDetails movieDetails) {
                mView.hideProgress();
                mView.showMovieDetails(movieDetails);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showError("Oops! Something went wrong. Please try again later.");
            }
        });
    }

    @Override
    public void onBackButtonClicked() {
        // TODO: 12/21/17 implement
    }
}
