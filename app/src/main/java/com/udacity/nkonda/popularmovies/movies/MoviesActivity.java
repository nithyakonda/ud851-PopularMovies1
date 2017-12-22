package com.udacity.nkonda.popularmovies.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;
import com.udacity.nkonda.popularmovies.moviedetails.MovieDetailsActivity;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View{
    private static final String TAG = "PM_MoviesActivity";
    private static final String PARAM_MOVIE_ID = "PARAM_MOVIE_ID";

    RecyclerView mRvMovieList;
    // TODO: 12/19/17 Replace ProgressDialog with swipe refresh layout
    ProgressDialog mDialog;

    MovieListAdapter mAdapter;
    MoviesPresenter mMoviesPresenter;

    List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Picasso.with(this).setLoggingEnabled(true);
        mRvMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        mDialog = new ProgressDialog(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRvMovieList.setLayoutManager(layoutManager);

        mAdapter = new MovieListAdapter();
        mAdapter.setOnItemClickedListener(new MovieListAdapter.OnItemClickedListener() {
            @Override
            public void onClick(int position) {
                startMovieDetailsActivity(mMovies.get(position).getId());
            }
        });
        mRvMovieList.setAdapter(mAdapter);

        mMoviesPresenter = new MoviesPresenter(MoviesRepository.getInstance(), this);
        mMoviesPresenter.load(SortOrder.Popular);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                mMoviesPresenter.load(SortOrder.Popular);
                return true;
            case R.id.action_sort_rating:
                mMoviesPresenter.load(SortOrder.TopRated);
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
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
    public void showResults(final List<Movie> movies) {
        mMovies = movies;
        mAdapter.setItems(mMovies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String errorMsg) {
        // TODO: 12/21/17 add alert dialog to show error
        Log.e(TAG, errorMsg);
    }

    private void startMovieDetailsActivity(int movieId) {
        Intent intent = new Intent();
        intent.putExtra(PARAM_MOVIE_ID, movieId);
        intent.setClass(this, MovieDetailsActivity.class);
        startActivity(intent);
    }
}
