package com.udacity.nkonda.popularmovies.data.source;

import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.Review;
import com.udacity.nkonda.popularmovies.data.Trailer;
import com.udacity.nkonda.popularmovies.movies.SortOrder;
import com.udacity.nkonda.popularmovies.utils.JsonHelper;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;

import java.net.URL;
import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public class MoviesRepository implements MoviesDataSource {
    private static final String TAG = "PM_MoviesRepository";

    private static MoviesRepository INSTANCE;

    private NetworkHelper mNetworkHelper = NetworkHelper.getInstance();

    private MoviesRepository() {
    }

    public static MoviesRepository getInstance() {
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
}
