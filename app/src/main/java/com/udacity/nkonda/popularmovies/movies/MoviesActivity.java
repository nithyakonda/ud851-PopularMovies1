package com.udacity.nkonda.popularmovies.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;
import com.udacity.nkonda.popularmovies.moviedetails.MovieDetailsActivity;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View{
    private static final String TAG = "PM_MoviesActivity";
    private static final String PARAM_MOVIE_ID = "PARAM_MOVIE_ID";
    private static final String SAVEKEY_LAST_PAGE_NUMBER = "SAVEKEY_LAST_PAGE_NUMBER";
    private static final String SAVEKEY_LAST_SORT_ORDER = "SAVEKEY_LAST_SORT_ORDER";

    RecyclerView mRvMovieList;
    // TODO: 12/19/17 Replace ProgressDialog with swipe refresh layout
    ProgressDialog mDialog;
    private static MoviesState mState = null;

    MovieListAdapter mAdapter;
    MoviesPresenter mPresenter;

    // TODO: 12/23/17 shouldn't be saving list here move to presenter
    List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        setContentView(R.layout.activity_main);
        Picasso.with(this).setLoggingEnabled(true);
        mRvMovieList = findViewById(R.id.rv_movie_list);
        mDialog = new ProgressDialog(this);

        RecyclerView.LayoutManager layoutManager = null;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, 5);
        } else {
            layoutManager = new GridLayoutManager(this, 3);
        }
        mRvMovieList.setLayoutManager(layoutManager);

        mAdapter = new MovieListAdapter();
        mAdapter.setOnItemClickedListener(new MovieListAdapter.OnItemClickedListener() {
            @Override
            public void onClick(int position) {
                startMovieDetailsActivity(mMovies.get(position).getId());
            }
        });
        mRvMovieList.setAdapter(mAdapter);

        mPresenter = new MoviesPresenter(MoviesRepository.getInstance(), this);

        if (inState != null) {
            mState = new MoviesState(
                    inState.getInt(SAVEKEY_LAST_PAGE_NUMBER),
                    (SortOrder) inState.getSerializable(SAVEKEY_LAST_SORT_ORDER)
            );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start(mState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MoviesState state = (MoviesState) mPresenter.getState();
        outState.putInt(SAVEKEY_LAST_PAGE_NUMBER, state.getLastPageNumber());
        outState.putSerializable(SAVEKEY_LAST_SORT_ORDER, state.getLastSortOrder());
    }

    @Override
    protected void onStop() {
        super.onStop();
        // TODO: 12/23/17 unsubscribe
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SortOrder newSortOrder;
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                newSortOrder = SortOrder.Popular;
                break;
            case R.id.action_sort_rating:
                newSortOrder = SortOrder.TopRated;
                break;
            default:
            return super.onOptionsItemSelected(item);
        }
        mPresenter.onSortOrderChanged(newSortOrder);
        return true;
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
