package com.udacity.nkonda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.R;

/**
 * Created by nkonda on 12/13/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder>{

    private Context mContext;

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        holder.bind(mContext);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;

        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }

        public void bind(Context context) {
            Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(ivThumbnail);
        }
    }
}
