/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity.nkonda.popularmovies.movies;

import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.constants.SortOrder;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.source.MoviesDataSource;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;

import java.util.List;

public class MoviesPresenter implements MoviesContract.Presenter {

    private final MoviesRepository mMoviesRepository;

    private final MoviesContract.View mMoviesView;

    private boolean mFirstLoad = true;

    public MoviesPresenter(@NonNull MoviesRepository moviesRepository, @NonNull MoviesContract.View moviesView) {
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
    }

    @Override
    public void start() {
        load(SortOrder.Popular);
    }

    @Override
    public void load(SortOrder sortOrder) {
        mMoviesView.showProgress();

        mMoviesRepository.getMovies(sortOrder, new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                mMoviesView.hideProgress();
                mMoviesView.showResults(movies);
            }

            @Override
            public void onDataNotAvailable() {
                mMoviesView.showError("Oops! Something went wrong. Please try again later.");
            }
        });
    }

    @Override
    public void onSortOrderChanged(SortOrder sortOrder) {
        load(sortOrder);
    }

    @Override
    public void onScrolledToEnd() {

    }

    @Override
    public void onMovieSelected() {

    }
}
