package com.udacity.nkonda.popularmovies.movies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.rafaelcrz.android_endless_scroll_lib.ScrollEndless;
import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.BaseActivity;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;
import com.udacity.nkonda.popularmovies.moviedetails.MovieDetailsActivity;

import java.util.List;

public class MoviesActivity extends BaseActivity implements MoviesContract.View{
    private static final String TAG = "PM_MoviesActivity";
    private static final String PARAM_MOVIE_ID = "PARAM_MOVIE_ID";
    private static final String SAVEKEY_LAST_PAGE_NUMBER = "SAVEKEY_LAST_PAGE_NUMBER";
    private static final String SAVEKEY_LAST_SORT_ORDER = "SAVEKEY_LAST_SORT_ORDER";

    private static MoviesState mState = null;

    private RecyclerView mRvMovieList;
    private SwipeRefreshLayout mSrlMovies;

    private ScrollEndless mEndlessScroll;
    private MovieListAdapter mAdapter;
    private MoviesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        setContentView(R.layout.activity_main);
        Picasso.with(this).setLoggingEnabled(true);
        mRvMovieList = findViewById(R.id.rv_movie_list);
        mSrlMovies = findViewById(R.id.srl_movies);

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
                mPresenter.onMovieSelected(position);
            }
        });
        mRvMovieList.setAdapter(mAdapter);
        mEndlessScroll = new ScrollEndless(this, mRvMovieList, (LinearLayoutManager) layoutManager);
        mEndlessScroll.addScrollEndless(new ScrollEndless.EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                int nextPage = mEndlessScroll.getPage() + 1;
                mEndlessScroll.setPage(nextPage);
                mPresenter.onScrolledToBottom();
            }

            @Override
            public void onLoadAllFinish() {

            }
        });

        mSrlMovies.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onScrolledToTop();
            }
        });

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
        mSrlMovies.setRefreshing(true);
        mEndlessScroll.isLoading(true);
    }

    @Override
    public void hideProgress() {
        mSrlMovies.setRefreshing(false);
        mEndlessScroll.isLoading(false);
    }

    @Override
    public void showResults(final List<Movie> movies, int totalPages) {
        mEndlessScroll.setTotalPage(totalPages);
        mAdapter.setItems(movies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMovieDetails(int movieId) {
        Intent intent = new Intent();
        intent.putExtra(PARAM_MOVIE_ID, movieId);
        intent.setClass(this, MovieDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showError(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.error_dialog_title)
                .setMessage(errorMsg)
                .setPositiveButton(getString(R.string.error_dialog_button_label),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.load();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mSrlMovies.setRefreshing(false);
                        Toast.makeText(MoviesActivity.this, R.string.toast_refresh_message, Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
