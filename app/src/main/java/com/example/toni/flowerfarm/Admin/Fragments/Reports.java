package com.example.toni.flowerfarm.Admin.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.toni.flowerfarm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by toni on 3/27/17.
 */

public class Reports extends Fragment {

    private TextView experts, farmers, posts;
    private View mView;

    private DatabaseReference mUsers;
    private DatabaseReference mPosts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_reports, container, false);

        //views
        experts = (TextView) mView.findViewById(R.id.tv_reports_experts);
        farmers = (TextView) mView.findViewById(R.id.tv_reports_farmers);
        posts = (TextView) mView.findViewById(R.id.tv_reports_posts);

        //firebase
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mPosts = FirebaseDatabase.getInstance().getReference().child("Posts");

        //get data
        getInfo();

        return mView;

    }

    private void getInfo() {
        //query to get total experts

        Query expert = mUsers.orderByChild("category").equalTo("expert");
        expert.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                experts.setText(String.valueOf(dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //query to get total farmers
        final Query farms = mUsers.orderByChild("category").equalTo("farmer");
        farms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                farmers.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //query to get total posts

        mPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                posts.setText(String.valueOf(dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
