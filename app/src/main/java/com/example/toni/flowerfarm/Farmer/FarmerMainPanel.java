package com.example.toni.flowerfarm.Farmer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.toni.flowerfarm.Intro.LoginActivity;
import com.example.toni.flowerfarm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FarmerMainPanel extends AppCompatActivity {

    private RecyclerView rv;
    private TextView indicator;
    private DatabaseReference mPosts;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_main_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FarmerMainPanel.this, PostFarmConditionActivity.class));
            }
        });

        //views
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        rv = (RecyclerView) findViewById(R.id.rv_farmer);
        rv.setLayoutManager(lm);
        indicator = (TextView) findViewById(R.id.tv_farmer_indicator);

        //Firebase...
        mPosts = FirebaseDatabase.getInstance().getReference().child("Posts");


        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(FarmerMainPanel.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
        Query q = mPosts.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());

        if (mAuth.getCurrentUser() != null) {

            FirebaseRecyclerAdapter<PostsModel, PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostsModel, PostsViewHolder>(

                    PostsModel.class,
                    R.layout.row_farm_posts,
                    PostsViewHolder.class,
                    q


            ) {
                @Override
                protected void populateViewHolder(PostsViewHolder viewHolder, PostsModel model, int position) {

                    if (model != null) {
                        String postId = getRef(position).getKey();
                        indicator.setVisibility(View.GONE);

                        viewHolder.setTexts(model);
                        viewHolder.setImage(getApplication(),model.getImage());

                        //open to one activity
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                    } else {

                        rv.setVisibility(View.GONE);
                    }

                }
            };

            rv.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        public TextView post_name, post_date, post_comment, post_feedback;
        public ImageView imageView;
        public View mView;
        private FirebaseAuth mAuth;


        public PostsViewHolder(View itemView) {
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

                post_feedback.setText("NO FEEDBACK RECEIVED YET");

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
