package com.udacity.nkonda.popularmovies.movies;

/**
 * Created by nkonda on 12/23/17.
 */

public class MoviesState implements MoviesContract.State {

    private final int mLastPageNumber;
    private final SortOrder mLastSortOrder;

    public MoviesState(int lastPageNumber, SortOrder lastSortOrder) {
        mLastPageNumber = lastPageNumber;
        mLastSortOrder = lastSortOrder;
    }

    @Override
    public int getLastPageNumber() {
        return mLastPageNumber;
    }

    @Override
    public SortOrder getLastSortOrder() {
        return mLastSortOrder;
    }
}
