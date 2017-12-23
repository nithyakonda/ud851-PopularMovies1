package com.udacity.nkonda.popularmovies.moviedetails;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.MovieDetails;
import com.udacity.nkonda.popularmovies.data.source.MoviesRepository;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsContract.View{
    private static final String TAG = "PM_MovieDetailsActivity";
    private static final String PARAM_MOVIE_ID = "PARAM_MOVIE_ID";

    ProgressDialog mDialog;
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

        mDialog = new ProgressDialog(this);
        mIvPoster = findViewById(R.id.iv_poster);
        mTvOriginalTitle = findViewById(R.id.tv_original_title);
        mTvReleaseDate = findViewById(R.id.tv_release_date);
        mTvRating = findViewById(R.id.tv_rating);
        mTvPlotSynopsis = findViewById(R.id.tv_plot_synopsis);

        mPresenter = new MovieDetailsPresenter(MoviesRepository.getInstance(), this);

        if (getIntent() != null && getIntent().hasExtra(PARAM_MOVIE_ID)) {
            int movieId = getIntent().getIntExtra(PARAM_MOVIE_ID, -1);
            mPresenter.load(movieId);
            Log.i(TAG, "Selected movie id::" + movieId);
        }
    }

    @Override
    public void showProgress() {
        mDialog.setMessage("Processing..");
    }

    @Override
    public void hideProgress() {
        mDialog.hide();
    }

    @Override
    public void showError(String errorMsg) {
        Log.e(TAG, errorMsg);
    }

    @Override
    public void showMovieDetails(MovieDetails movieDetails) {
        String url = NetworkHelper.getInstance().getUrl(movieDetails.getPosterPath());
        Picasso.with(this).load(url).into(mIvPoster);
        mTvOriginalTitle.setText(movieDetails.getOriginalTitle());
        mTvReleaseDate.setText(movieDetails.getReleaseDate());
        mTvRating.setText(String.valueOf(movieDetails.getRating()));
        mTvPlotSynopsis.setText(movieDetails.getPlotSynopsis());
    }
}
