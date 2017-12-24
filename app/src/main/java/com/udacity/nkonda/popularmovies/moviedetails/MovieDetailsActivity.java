package com.udacity.nkonda.popularmovies.moviedetails;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.BaseActivity;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;

public class MovieDetailsActivity extends BaseActivity implements MovieDetailsContract.View{
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

    MovieDetailsPresenter mPresenter;

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
        mTvPlotSynopsis.setMovementMethod(new ScrollingMovementMethod());

        mPresenter = new MovieDetailsPresenter(MoviesRepository.getInstance(), this);

        if (getIntent() != null && getIntent().hasExtra(PARAM_MOVIE_ID)) {
            movieId = getIntent().getIntExtra(PARAM_MOVIE_ID, -1);
            mPresenter.load(movieId);
            Log.i(TAG, "Selected movie id::" + movieId);
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
}
