package com.example.toni.flowerfarm.Admin.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.toni.flowerfarm.Admin.Model.ExpertsModel;
import com.example.toni.flowerfarm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by toni on 3/26/17.
 */

public class Experts extends Fragment {

    private RecyclerView rv;
    private TextView indicator;
    private View mView;

    private FirebaseAuth mAuth;
    private DatabaseReference mExperts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_experts,container,false);

        //views
        rv = (RecyclerView) mView.findViewById(R.id.rv_experts);
        indicator = (TextView) mView.findViewById(R.id.tv_experts_indicator);



        //set up rv
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);

        rv.setLayoutManager(lm);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mExperts = FirebaseDatabase.getInstance().getReference().child("Users");

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){

            Query q = mExperts.orderByChild("category").equalTo("expert");

            FirebaseRecyclerAdapter <ExpertsModel,ExpertsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ExpertsModel, ExpertsViewHolder>(

                    ExpertsModel.class,
                    R.layout.row_experts,
                    ExpertsViewHolder.class,
                    q

            ) {
                @Override
                protected void populateViewHolder(ExpertsViewHolder viewHolder, ExpertsModel model, int position) {

                    if (model != null){

                        indicator.setVisibility(View.GONE);

                        viewHolder.setTextx(model);

                    }else {
                        rv.setVisibility(View.GONE);
                    }

                }
            };

            rv.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.notifyDataSetChanged();

        }
    }

    public static class ExpertsViewHolder extends RecyclerView.ViewHolder{

        private TextView name, email, password;
        private View mView;

        public ExpertsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            name = (TextView) mView.findViewById(R.id.tv_experts_name);
            email = (TextView) mView.findViewById(R.id.tv_experts_email);
            password = (TextView) mView.findViewById(R.id.tv_experts_pass);
        }

        public void setTextx(ExpertsModel experts){
            name.setText(experts.getName());
            email.setText(experts.getEmail());
            password.setText(experts.getPassword());
        }

    }
}
