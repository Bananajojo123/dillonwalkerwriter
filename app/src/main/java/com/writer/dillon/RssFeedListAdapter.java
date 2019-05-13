package com.writer.dillon;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by obaro on 27/11/2016.
 */

public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RssFeedModel> rssFeedModels;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View rssFeedView;
        private static final String TAG = RssFeedListAdapter.class.getSimpleName();

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }


        @Override
        public void onClick(View view) {
            Log.i(TAG, "clicked");
        }
    }

    public RssFeedListAdapter(List<RssFeedModel> rssFeedModels) {
        this.rssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = rssFeedModels.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);
        ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(rssFeedModel.description);
        ((TextView)holder.rssFeedView.findViewById(R.id.linkText)).setText(rssFeedModel.link);
    }



    @Override
    public int getItemCount() {
        return rssFeedModels.size();
    }


}

