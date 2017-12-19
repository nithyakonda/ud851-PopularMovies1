package com.udacity.nkonda.popularmovies.movies;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.adapters.MovieListAdapter;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View{
    private static final String TAG = "PM_MoviesActivity";

    RecyclerView mRvMovieList;
    // TODO: 12/19/17 Replace ProgressDialog with swipe refresh layout
    ProgressDialog mDialog;

    MoviesPresenter mMoviesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Picasso.with(this).setLoggingEnabled(true);
        mRvMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        mDialog = new ProgressDialog(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRvMovieList.setLayoutManager(layoutManager);

        mMoviesPresenter = new MoviesPresenter(MoviesRepository.getInstance(), this);
        mMoviesPresenter.load();
    }

    @Override
    public void showProgress() {
        mDialog.setMessage("Processing..");
    }

    @Override
    public void hideProgress() {
        mDialog.dismiss();
    }

    @Override
    public void showResults(List<Movie> movies) {
        MovieListAdapter adapter = new MovieListAdapter(movies);
        mRvMovieList.setAdapter(adapter);
    }

    @Override
    public void showError(String errorMsg) {
        Log.e(TAG, "Network error");
    }

    // UI helper methods
}
