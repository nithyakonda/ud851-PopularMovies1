package com.udacity.nkonda.popularmovies.moviedetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.rafaelcrz.android_endless_scroll_lib.ScrollEndless;
import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.BaseActivity;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.adapters.ReviewsAdapter;
import com.udacity.nkonda.popularmovies.adapters.TrailersAdapter;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.Review;
import com.udacity.nkonda.popularmovies.data.Trailer;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;

import java.util.List;

public class MovieDetailsActivity extends BaseActivity
        implements MovieDetailsContract.View,
                   View.OnClickListener{
    private static final String TAG = "PM_MovieDetailsActivity";
    private static final String PARAM_MOVIE_ID = "PARAM_MOVIE_ID";

    private static int movieId;

    LinearLayout mContentLayout;
    ProgressBar mPbIndicator;
    ImageView mIvPoster;
    TextView mTvOriginalTitle;
    TextView mTvReleaseDate;
    TextView mTvRating;
    TextView mTvPlotSynopsis;
    TextView mTvCurrentPage;
    TextView mTvTotalPages;
    RecyclerView mRvTrailersList;
    RecyclerView mRvReviewsList;

    MovieDetailsPresenter mPresenter;
    TrailersAdapter mTrailersAdapter;
    ReviewsAdapter mReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mContentLayout = findViewById(R.id.content_layout);
        mPbIndicator = findViewById(R.id.pb_indicator);
        mIvPoster = findViewById(R.id.iv_poster);
        mTvOriginalTitle = findViewById(R.id.tv_original_title);
        mTvReleaseDate = findViewById(R.id.tv_release_date);
        mTvRating = findViewById(R.id.tv_rating);
        mTvPlotSynopsis = findViewById(R.id.tv_plot_synopsis);
        mRvTrailersList = findViewById(R.id.rv_trailer_list);
        mRvReviewsList = findViewById(R.id.rv_review_list);
        mTvCurrentPage = findViewById(R.id.tv_current_page);
        mTvTotalPages = findViewById(R.id.tv_total_pages);

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);

        mTvPlotSynopsis.setMovementMethod(new ScrollingMovementMethod());
        RecyclerView.LayoutManager trailersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvTrailersList.setLayoutManager(trailersLayoutManager);
        mTrailersAdapter = new TrailersAdapter();
        mTrailersAdapter.setOnItemClickedListener(new TrailersAdapter.OnItemClickedListener() {
            @Override
            public void onClick(int position) {
                mPresenter.onTrailerSelected(position);
            }
        });
        mRvTrailersList.setAdapter(mTrailersAdapter);

        RecyclerView.LayoutManager reviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvReviewsList.setLayoutManager(reviewsLayoutManager);
        mReviewsAdapter = new ReviewsAdapter();
        mRvReviewsList.setAdapter(mReviewsAdapter);

        mPresenter = new MovieDetailsPresenter(MoviesRepository.getInstance(), this);

        if (getIntent() != null && getIntent().hasExtra(PARAM_MOVIE_ID)) {
            movieId = getIntent().getIntExtra(PARAM_MOVIE_ID, -1);
            mPresenter.load(movieId);
            mPresenter.loadTrailers(movieId);
            mPresenter.loadReviews(movieId);
            Log.i(TAG, "Selected movie id::" + movieId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        if (item.getItemId() == R.id.action_favourite) {
            mPresenter.saveToFavourites(movieId);
            item.setIcon(R.drawable.ic_action_favourite_on);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                mPresenter.onBackButtonClicked();
                break;
            case R.id.btn_next:
                mPresenter.onNextButtonClicked();
                break;
        }
    }

    @Override
    public void showProgress() {
        mPbIndicator.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        mPbIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String errorMsg) {
        mContentLayout.setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.error_dialog_title)
                .setMessage(errorMsg)
                .setPositiveButton(getString(R.string.error_dialog_button_label),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.load(movieId);
                            }
                        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        hideProgress();
                        mIvPoster.setBackgroundColor(getResources().getColor(R.color.placeholder));
                        mContentLayout.setVisibility(View.VISIBLE);
                    }
                });
        builder.create().show();
    }

    @Override
    public void showMovieDetails(MovieDetails movieDetails) {
        mContentLayout.setVisibility(View.VISIBLE);
        setTitle(movieDetails.getOriginalTitle());
        String url = NetworkHelper.getInstance().getUrl(movieDetails.getPosterPath());
        Picasso.with(this)
                .load(url)
                .placeholder(R.color.placeholder)
                .into(mIvPoster);
        mTvOriginalTitle.setText(movieDetails.getOriginalTitle());
        mTvReleaseDate.setText(movieDetails.getReleaseDate());
        mTvRating.setText(String.valueOf(movieDetails.getRating()));
        mTvPlotSynopsis.setText(movieDetails.getPlotSynopsis());
    }

    @Override
    public void showTrailers(List<Trailer> trailers) {
        mTrailersAdapter.setItems(trailers);
        mTrailersAdapter.notifyDataSetChanged();
    }

    @Override
    public void showReviews(List<Review> reviews, int currentPage, int totalPages) {
        mTvCurrentPage.setText(String.valueOf(currentPage));
        mTvTotalPages.setText(String.valueOf(totalPages));
        mReviewsAdapter.setItems(reviews);
        mReviewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void playTrailer(Uri trailerUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
        startActivity(intent);
    }
}
