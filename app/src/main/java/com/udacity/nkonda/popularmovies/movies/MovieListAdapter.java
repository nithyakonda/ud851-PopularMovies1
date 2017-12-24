package com.udacity.nkonda.popularmovies.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.Movie;
import com.udacity.nkonda.popularmovies.utils.NetworkHelper;

import java.util.List;

/**
 * Created by nkonda on 12/13/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder>{

    private final static String TMDB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    private Context mContext;
    private OnItemClickedListener mListener;

    private List<Movie> mMovies;

    public MovieListAdapter() {
    }

    public void setItems(List<Movie> movies) {
        mMovies = movies;
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mListener = listener;
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
        return mMovies == null ? 20 : mMovies.size();
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivThumbnail;

        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            itemView.setOnClickListener(this);
        }

        public void bind(Context context, int position) {
            String url = null;
            if (mMovies != null) {
                Movie movie = mMovies.get(position);
                url = NetworkHelper.getInstance().getUrl(movie.getPosterPath());
            }
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.color.placeholder)
                    .into(ivThumbnail);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mListener != null) {
                mListener.onClick(position);
            }
        }
    }

    public interface OnItemClickedListener {
        void onClick(int position);
    }
}
