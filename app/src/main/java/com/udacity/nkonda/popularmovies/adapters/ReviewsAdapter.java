package com.udacity.nkonda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.Review;

import java.util.List;

/**
 * Created by nkonda on 2/25/18.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewholder>{
    private Context mContext;
    private List<Review> mReviews;

    public void setItems(List<Review> reviews) {
        mReviews = reviews;
    }

    @Override
    public ReviewsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewsViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewsViewholder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReviews == null ? 0 : mReviews.size();
    }

    public class ReviewsViewholder extends RecyclerView.ViewHolder {
        TextView tvAuthor;
        TextView tvContent;

        public ReviewsViewholder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvContent = itemView.findViewById(R.id.tv_content);
        }

        public void bind(int position) {
            Review review = mReviews.get(position);
            tvAuthor.setText(review.getAuthor());
            tvContent.setText(review.getContent());
        }
    }
}
