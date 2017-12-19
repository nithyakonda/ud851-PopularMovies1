package com.udacity.nkonda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by nkonda on 12/13/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder>{

    private final static String TMDB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    private Context mContext;
    private List<Movie> mMovies;

    public MovieListAdapter(List<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        holder.bind(mContext, position);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;

        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }

        public void bind(Context context, int position) {
            Movie movie = mMovies.get(position);
            Picasso.with(context).load(TMDB_BASE_IMAGE_URL + movie.getPosterPath()).into(ivThumbnail);
        }
    }
}
