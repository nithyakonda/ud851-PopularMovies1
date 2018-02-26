package com.udacity.nkonda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.nkonda.popularmovies.R;
import com.udacity.nkonda.popularmovies.data.Trailer;

import java.util.List;

/**
 * Created by nkonda on 2/25/18.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder>{
    private Context mContext;
    private OnItemClickedListener mListener;

    private List<Trailer> mTrailers;

    public void setItems(List<Trailer> trailers) {
        mTrailers = trailers;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.trailer_list_item, parent, false);
        return new TrailersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTrailers == null ? 0 : mTrailers.size();
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mListener = listener;
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTrailerName;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            tvTrailerName = itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            tvTrailerName.setText(mTrailers.get(position).getName());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                mListener.onClick(position);
            }
        }
    }

    public interface OnItemClickedListener {
        void onClick(int position);
    }
}
