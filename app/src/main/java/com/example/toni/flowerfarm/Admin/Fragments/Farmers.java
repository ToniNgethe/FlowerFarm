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

import com.example.toni.flowerfarm.Admin.Model.FarmerModel;
import com.example.toni.flowerfarm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by toni on 3/26/17.
 */

public class Farmers extends Fragment {

    private RecyclerView rv;
    private View mView;
    private DatabaseReference mUsers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_farmers, container, false);

        //rv
        rv = (RecyclerView) mView.findViewById(R.id.rv_farmers);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);

        rv.setLayoutManager(lm);

        //Firebase..

        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");


        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = mUsers.orderByChild("category").equalTo("farmer");

        FirebaseRecyclerAdapter<FarmerModel,FarmersVieHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FarmerModel, FarmersVieHolder>(
                FarmerModel.class,
                R.layout.row_farmers,
                FarmersVieHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(FarmersVieHolder viewHolder, FarmerModel model, int position) {

                viewHolder.setInfo(model);

            }
        };

        rv.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }

    public static class FarmersVieHolder extends RecyclerView.ViewHolder{

        private TextView name, farmName, email, id;
        private View mView;

        public FarmersVieHolder(View itemView) {
            super(itemView);

            mView = itemView;

            //views
            name = (TextView) mView.findViewById(R.id.tv_farmers_name);
            farmName = (TextView) mView.findViewById(R.id.tv_farmers_farmname);
            email = (TextView) mView.findViewById(R.id.tv_farmers_email);
            id = (TextView) mView.findViewById(R.id.tv_farmers_id);
        }

        public void setInfo(FarmerModel model){

            name.setText(model.getName());
            farmName.setText(model.getFarm_name());
            email.setText(model.getMobile_number());
            id.setText(model.getId_number());

        }
    }
}
