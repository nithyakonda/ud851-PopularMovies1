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

import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.source.MoviesDataSource;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;

import java.util.List;

public class MoviesPresenter implements MoviesContract.Presenter {

    private static final SortOrder mDefaultSortOrder = SortOrder.Popular;
    private static final int mDefaultPageNumber = 1;

    private final MoviesRepository mMoviesRepository;

    private final MoviesContract.View mView;

    private static int mLastPageNumber = mDefaultPageNumber;
    private static SortOrder mLastSortOrder = mDefaultSortOrder;

    public MoviesPresenter(@NonNull MoviesRepository moviesRepository, @NonNull MoviesContract.View view) {
        mMoviesRepository = moviesRepository;
        mView = view;
    }

    @Override
    public void start(MoviesContract.State state) {
        if (state != null) {
            mLastPageNumber = state.getLastPageNumber();
            mLastSortOrder = state.getLastSortOrder();
        }
        load();
    }

    @Override
    public MoviesContract.State getState() {
        return new MoviesState(mLastPageNumber, mLastSortOrder);
    }

    @Override
    public void load() {
        if (mView.isOnline()) {
            mView.showProgress();

            mMoviesRepository.getMovies(mLastSortOrder, mLastPageNumber, new MoviesDataSource.GetMoviesCallback() {
                @Override
                public void onMoviesLoaded(List<Movie> movies, int totalPages) {
                    mView.hideProgress();
                    mView.showResults(movies, totalPages);
                }

                @Override
                public void onDataNotAvailable() {
                    mView.showError("Something went wrong.");
                }
            });
        } else {
            mView.showError("You are not connected to the internet");
        }
    }

    @Override
    public void onSortOrderChanged(SortOrder sortOrder) {
        mLastSortOrder = sortOrder;
        load();
    }

    @Override
    public void onScrolledToTop() {
        if (mLastPageNumber > 1) {
            mLastPageNumber--;
        }
        load();
    }

    @Override
    public void onScrolledToBottom() {
        mLastPageNumber++;
        load();
    }
}
