package com.example.toni.flowerfarm.Expert;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toni.flowerfarm.Farmer.PostsModel;
import com.example.toni.flowerfarm.Intro.LoginActivity;
import com.example.toni.flowerfarm.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class VerifyyingUserActivity extends AppCompatActivity {

    private TextView indicator;
    private RecyclerView rv;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyying_user);

        indicator = (TextView) findViewById(R.id.tv_verifying_indicator);
        rv = (RecyclerView) findViewById(R.id.rv_verifying);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);

        rv.setLayoutManager(lm);

        mAuth = FirebaseAuth.getInstance();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(VerifyyingUserActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query q = mUsers.orderByChild("category").equalTo("farmer");

        FirebaseRecyclerAdapter<PostsModel,VerifyUserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostsModel, VerifyUserViewHolder>(

                PostsModel.class,
                R.layout.row_verifies,
                VerifyUserViewHolder.class,
                q

        ) {
            @Override
            protected void populateViewHolder(VerifyUserViewHolder viewHolder, PostsModel model, int position) {

                if (model != null){

                    final String userId = getRef(position).getKey();

                    indicator.setVisibility(View.GONE);
                    viewHolder.info(model);
                    viewHolder.setUserVerified(userId);

                    viewHolder.verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final boolean[] done = {true};

                            DatabaseReference db = mUsers.child(userId);

                            db.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (done[0]){

                                        dataSnapshot.getRef().child("status").setValue(1);
                                        done[0] = false;

                                        Toast.makeText(VerifyyingUserActivity.this,"Farmer verified",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                }else {
                    rv.setVisibility(View.GONE);
                }

            }
        };

        rv.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }

    public static class VerifyUserViewHolder extends RecyclerView.ViewHolder{

        private TextView name, number;
        private Button verify;

        private DatabaseReference mPosts;


        public VerifyUserViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_verify_name);
            number = (TextView) itemView.findViewById(R.id.tv_verify_number);
            verify = (Button) itemView.findViewById(R.id.btn_verify);

            mPosts = FirebaseDatabase.getInstance().getReference().child("Users");
        }

        public void info(PostsModel model){
            name.setText(model.getName());
            number.setText(model.getMobile_number());
        }

        public void setUserVerified(String post){

            mPosts.child(post).child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue().toString() == "1"){
                        verify.setText("Verified");
                        verify.setEnabled(false);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
