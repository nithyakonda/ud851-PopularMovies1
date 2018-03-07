package com.udacity.nkonda.popularmovies.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.Review;
import com.udacity.nkonda.popularmovies.data.Trailer;
import com.udacity.nkonda.popularmovies.data.sqlite.MoviesDbContract;
import com.udacity.nkonda.popularmovies.data.sqlite.QueryDbAsyncTask;
import com.udacity.nkonda.popularmovies.movies.SortOrder;
import com.udacity.nkonda.popularmovies.utils.JsonHelper;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;
import com.udacity.nkonda.popularmovies.utils.Runner;

import java.net.URL;
import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public class MoviesRepository implements MoviesDataSource {
    private static final String TAG = "PM_MoviesRepository";

    private static MoviesRepository INSTANCE;

    private static Context mContext;
    private NetworkHelper mNetworkHelper = NetworkHelper.getInstance();

    private MoviesRepository() {
    }

    public static MoviesRepository getInstance(Context context) {
        mContext = context;
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository();
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(SortOrder sortOrder, int pageNo, @NonNull final GetMoviesCallback callback) {
        URL getMoviesUrl = mNetworkHelper.getUrl(sortOrder, pageNo);
        mNetworkHelper.startHttpRequestTask(getMoviesUrl,
            new NetworkHelper.OnHttpResponseListener() {
                @Override
                public void onReceive(String httpResponse) {
                    if (httpResponse == null) {
                        callback.onDataNotAvailable();
                    } else {
                        int totalPages = JsonHelper.getTotalPages(httpResponse);
                        if (totalPages > 0) {
                            List<Movie> movies = JsonHelper.parseMoviesListJson(httpResponse);
                            if (movies.isEmpty()) {
                                callback.onDataNotAvailable();
                            } else {
                                callback.onMoviesLoaded(movies, totalPages);
                            }
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                }
            });
    }

    @Override
    public void getMovieDetails(@NonNull int movieId, @NonNull final GetMovieDetailsCallback callback) {
        URL getMovieDetailsUrl = mNetworkHelper.getMovieDetailsUrl(movieId);
        mNetworkHelper.startHttpRequestTask(getMovieDetailsUrl,
            new NetworkHelper.OnHttpResponseListener() {
                @Override
                public void onReceive(String httpResponse) {
                    if (httpResponse == null) {
                        callback.onDataNotAvailable();
                    } else {
                        MovieDetails movieDetails = JsonHelper.parseMovieDetailsJson(httpResponse);
                        if (movieDetails == null) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onMovieDetailsLoaded(movieDetails);
                        }
                    }
                }
            });
    }

    @Override
    public void getTrailers(@NonNull int movieId, @NonNull final GetTrailersCallback callback) {
        URL trailersUrl = mNetworkHelper.getTrailersUrl(movieId);
        mNetworkHelper.startHttpRequestTask(trailersUrl,
                new NetworkHelper.OnHttpResponseListener() {
                    @Override
                    public void onReceive(String httpResponse) {
                        if (httpResponse == null) {
                            callback.onDataNotAvailable();
                        } else {
                            List<Trailer> trailers = JsonHelper.parseTrailersJson(httpResponse);
                            if (trailers == null) {
                                callback.onDataNotAvailable();
                            } else {
                                callback.onTrailersLoaded(trailers);
                            }
                        }
                    }
                });
    }

    @Override
    public void getReviews(@NonNull int movieId, int pageNo, @NonNull final GetReviewsCallback callback) {
        URL reviewsUrl = mNetworkHelper.getReviewsUrl(movieId, pageNo);
        mNetworkHelper.startHttpRequestTask(reviewsUrl,
                new NetworkHelper.OnHttpResponseListener() {
                    @Override
                    public void onReceive(String httpResponse) {
                        if (httpResponse == null) {
                            callback.onDataNotAvailable();
                        } else {
                            int totalPages = JsonHelper.getTotalPages(httpResponse);
                            if (totalPages > 0) {
                                List<Review> reviews = JsonHelper.parseReviewsJson(httpResponse);
                                if (reviews == null) {
                                    callback.onDataNotAvailable();
                                } else {
                                    callback.onReviewsLoaded(reviews, totalPages);
                                }
                            } else {
                                callback.onDataNotAvailable();
                            }
                        }
                    }
                });
    }

    @Override
    public void getFavoriteMovies(@NonNull GetFavoriteMoviesCallback callback) {
        Uri uri = MoviesDbContract.MovieEntry.CONTENT_URI;
        QueryDbAsyncTask queryDbTask = new QueryDbAsyncTask(mContext, uri, callback);
        queryDbTask.execute();
    }

    @Override
    public void findFavoriteMovie(int movieId, @NonNull final FindFavoriteMovieCallback callback) {
        String movieIdStr = Integer.toString(movieId);
        Uri uri = MoviesDbContract.MovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath(movieIdStr)
                .build();
        QueryDbAsyncTask queryDbTask = new QueryDbAsyncTask(mContext, uri, new GetFavoriteMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                if (movies.size() != 0) {
                    callback.onMovieFound(true);
                } else {
                    callback.onMovieFound(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                callback.onMovieFound(false);
            }
        });
        queryDbTask.execute();
    }

    @Override
    public void addFavoriteMovie(Movie movie) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesDbContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MoviesDbContract.MovieEntry.COLUMN_MOVIE_NAME, movie.getTitle());
        contentValues.put(MoviesDbContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        Runner.getInstance().runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mContext.getContentResolver().insert(MoviesDbContract.MovieEntry.CONTENT_URI, contentValues);
            }
        });
    }

    @Override
    public void removeFavoriteMovie(int movieId) {
        final Uri uri = MoviesDbContract.MovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath(Integer.toString(movieId))
                .build();
        Runner.getInstance().runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mContext.getContentResolver().delete(uri, null, null);
            }
        });
    }
}
