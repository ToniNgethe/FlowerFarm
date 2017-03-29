package com.example.toni.flowerfarm.Expert;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.toni.flowerfarm.Expert.Dialog.FeedBackPanel;
import com.example.toni.flowerfarm.Farmer.PostsModel;
import com.example.toni.flowerfarm.Intro.LoginActivity;
import com.example.toni.flowerfarm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExpertPanelActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_verify);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ExpertPanelActivity.this,VerifyyingUserActivity.class));

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_expertpanel);

        //linear
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        lm.setReverseLayout(true);
        recyclerView.setLayoutManager(lm);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ExpertPanelActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        };

        mPosts = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:

                mAuth.signOut();
                startActivity(new Intent(ExpertPanelActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

        if (mAuth != null) {

            FirebaseRecyclerAdapter<PostsModel, ExpertPanelViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostsModel, ExpertPanelViewHolder>(

                    PostsModel.class,
                    R.layout.row_expertspanel,
                    ExpertPanelViewHolder.class,
                    mPosts

            ) {
                @Override
                protected void populateViewHolder(ExpertPanelViewHolder viewHolder, PostsModel model, int position) {

                    final String postID = getRef(position).getKey();

                    viewHolder.setInfo(model);
                    viewHolder.setImage(getApplication(), model.getImage());

                    viewHolder.add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FeedBackPanel feedBackPanel = new FeedBackPanel(ExpertPanelActivity.this, postID);
                            feedBackPanel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            feedBackPanel.setCanceledOnTouchOutside(false);
                            feedBackPanel.setCancelable(false);
                            feedBackPanel.show();
                        }
                    });

                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public static class ExpertPanelViewHolder extends RecyclerView.ViewHolder {


        private TextView name, date, comment, review;
        private ImageView image;
        public Button add;
        private View mView;

        public ExpertPanelViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            name = (TextView) mView.findViewById(R.id.tv_expertspanel_name);
            date = (TextView) mView.findViewById(R.id.tv_expertspanel_date);
            comment = (TextView) mView.findViewById(R.id.tv_expertspanel_comment);
            review = (TextView) mView.findViewById(R.id.tv_expertspanel_review);
            add = (Button) mView.findViewById(R.id.btn_expertspanel_review);
            image = (ImageView) mView.findViewById(R.id.iv_expertspanel_image);
        }

        public void setInfo(PostsModel model) {
            name.setText(model.getName());
            date.setText(model.getDate());
            comment.setText(model.getComment());

            if (model.getFeedback() == null) {
                review.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
            } else {
                review.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);
                review.setText(model.getFeedback());

            }
        }

        public void setImage(final Context ctx, final String url) {

            Glide.with(ctx).load(url)
                    .crossFade()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }
    }

}
