package com.writer.dillon;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by obaro on 27/11/2016.
 */

public class BlogAdapter
        extends RecyclerView.Adapter<BlogAdapter.FeedModelViewHolder> {

    private List<Blog> blogs;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View rssFeedView;
        private static final String TAG = BlogAdapter.class.getSimpleName();

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }


        @Override
        public void onClick(View view) {
            Log.i(TAG, "clicked");
        }
    }

    public BlogAdapter(List<Blog> blogs) {
        this.blogs = blogs;
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
        final Blog blog = blogs.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(blog.title);
        ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(blog.description);
        ((TextView)holder.rssFeedView.findViewById(R.id.linkText)).setText(blog.link);
    }



    @Override
    public int getItemCount() {
        return blogs.size();
    }


}

