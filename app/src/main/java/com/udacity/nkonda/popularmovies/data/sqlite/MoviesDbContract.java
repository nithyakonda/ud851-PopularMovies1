package com.udacity.nkonda.popularmovies.data.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nkonda on 2/27/18.
 */

public final class MoviesDbContract {
    public static final String AUTHORITY = "com.udacity.nkonda.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    private MoviesDbContract() {
    }

    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
    }
}
