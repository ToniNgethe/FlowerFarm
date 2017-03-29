package com.example.toni.flowerfarm.Admin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.toni.flowerfarm.Farmer.PostsModel;
import com.example.toni.flowerfarm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by toni on 3/26/17.
 */

public class FarmPosts extends Fragment {

    private View mView;
    private RecyclerView rv;
    private FirebaseAuth mAuth;
    private DatabaseReference mPosts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_farmposts, container, false);

        //rv
        rv = (RecyclerView) mView.findViewById(R.id.rv_farmposts);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);

        rv.setLayoutManager(lm);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mPosts = FirebaseDatabase.getInstance().getReference().child("Posts");

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {

            FirebaseRecyclerAdapter<PostsModel, FarmPostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostsModel, FarmPostsViewHolder>(

                    PostsModel.class,
                    R.layout.row_farm_posts,
                    FarmPostsViewHolder.class,
                    mPosts

            ) {
                @Override
                protected void populateViewHolder(FarmPostsViewHolder viewHolder, PostsModel model, int position) {

                    viewHolder.setTexts(model);
                    viewHolder.setImage(getActivity(), model.getImage());

                }
            };

            rv.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.notifyDataSetChanged();

        }
    }

    public static class FarmPostsViewHolder extends RecyclerView.ViewHolder {


        public TextView post_name, post_date, post_comment, post_feedback;
        public ImageView imageView;
        public View mView;
        private FirebaseAuth mAuth;


        public FarmPostsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mAuth = FirebaseAuth.getInstance();

            //views
            post_name = (TextView) mView.findViewById(R.id.tv_posts_name);
            post_date = (TextView) mView.findViewById(R.id.tv_post_date);
            post_comment = (TextView) mView.findViewById(R.id.tv_posts_comment);
            post_feedback = (TextView) mView.findViewById(R.id.tv_post_feedback);
            imageView = (ImageView) mView.findViewById(R.id.iv_posts_image);
        }

        public void setTexts(PostsModel model) {

            post_name.setText(model.getName());
            post_date.setText(model.getDate());
            post_comment.setText(model.getComment());

            if (model.getFeedback() != null) {
                post_feedback.setText(model.getFeedback());
            } else {

                post_feedback.setText("NO FEEDBACK ADDED YET");

            }

        }

        public void setImage(final Context ctx, final String url) {
            Glide.with(ctx).load(url)
                    .crossFade()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

        }
    }
}
