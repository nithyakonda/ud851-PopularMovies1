package com.udacity.nkonda.popularmovies.data.source;

import android.support.annotation.NonNull;

import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.Review;
import com.udacity.nkonda.popularmovies.data.Trailer;
import com.udacity.nkonda.popularmovies.movies.SortOrder;

import java.util.List;

/**
 * Created by nkonda on 12/18/17.
 */

public interface MoviesDataSource {

    interface GetMoviesCallback {

        void onMoviesLoaded(List<Movie> movies, int totalPages);

        void onDataNotAvailable();
    }

    interface GetMovieDetailsCallback {

        void onMovieDetailsLoaded(MovieDetails movieDetails);

        void onDataNotAvailable();
    }

    interface GetTrailersCallback {

        void onTrailersLoaded(List<Trailer> trailers);

        void onDataNotAvailable();
    }

    interface GetReviewsCallback {

        void onReviewsLoaded(List<Review> reviews, int totalPages);

        void onDataNotAvailable();
    }

    interface GetFavoriteMoviesCallback {

        void onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface FindFavoriteMovieCallback {
        void onMovieFound(boolean isFavorite);
    }

    void getMovies(SortOrder sortOrder, int pageNo, @NonNull GetMoviesCallback callback);

    void getMovieDetails(@NonNull int movieId, @NonNull GetMovieDetailsCallback callback);

    void getTrailers(@NonNull int movieId, @NonNull GetTrailersCallback callback);

    void getReviews(@NonNull int movieId, int pageNo, @NonNull GetReviewsCallback callback);

    void getFavoriteMovies(@NonNull GetFavoriteMoviesCallback callback);

    void findFavoriteMovie(int movieId, @NonNull FindFavoriteMovieCallback callback);

    void addFavoriteMovie(Movie movie);

    void removeFavoriteMovie(int movieId);
}
