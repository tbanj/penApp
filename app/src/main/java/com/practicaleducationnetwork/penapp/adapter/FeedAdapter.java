package com.practicaleducationnetwork.penapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedHolder> {

    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FeedHolder extends RecyclerView.ViewHolder {

        public FeedHolder(View itemView) {
            super(itemView);

        }
    }

}
