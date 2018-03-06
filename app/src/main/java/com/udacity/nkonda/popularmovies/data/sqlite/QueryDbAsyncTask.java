package com.udacity.nkonda.popularmovies.data.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.source.MoviesDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nkonda on 2/28/18.
 */

public class QueryDbAsyncTask extends AsyncTask<Void, Void, Cursor> {
    private static final String TAG = QueryDbAsyncTask.class.getSimpleName();

    private Context mContext;
    private MoviesDataSource.GetFavoriteMoviesCallback mCallback;
    private Uri mUri;

    public QueryDbAsyncTask(Context context,
            Uri uri,
            MoviesDataSource.GetFavoriteMoviesCallback callback) {
        mContext = context;
        mUri = uri;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        if (cursor.getCount() > 0) {
            mCallback.onMoviesLoaded(getMovieListFromCursor(cursor));
        } else {
            mCallback.onDataNotAvailable();
        }
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        try {
            return mContext.getContentResolver().query(mUri,
                    null,
                    null,
                    null,
                    null);

        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    private List<Movie> getMovieListFromCursor(Cursor cursor) {
        List<Movie> favoriteMovies = new ArrayList<>();
        Movie movie;
        int movieNameIndex = cursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_MOVIE_ID);
        int movieIdIndex = cursor.getColumnIndex(MoviesDbContract.MovieEntry.COLUMN_MOVIE_NAME);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            int id = cursor.getInt(movieIdIndex);
            String name = cursor.getString(movieNameIndex);
            movie = new Movie(id, name);
            movie.setFavorite(true);
            favoriteMovies.add(movie);
        }
        return favoriteMovies;
    }
}
