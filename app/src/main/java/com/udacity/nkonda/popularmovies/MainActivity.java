package com.udacity.nkonda.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.adapters.MovieListAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRvMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Picasso.with(this).setLoggingEnabled(true);
        mRvMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        setupRecyclerView();
    }

    // UI helper methods
    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        MovieListAdapter adapter = new MovieListAdapter();
        mRvMovieList.setLayoutManager(layoutManager);
        mRvMovieList.setAdapter(adapter);
    }
}
