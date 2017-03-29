package com.example.toni.flowerfarm.Farmer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.toni.flowerfarm.Intro.LoginActivity;
import com.example.toni.flowerfarm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmationActivity extends AppCompatActivity {

    private Button check;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;
    private DatabaseReference mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);



        //view
        check = (Button) findViewById(R.id.btn_confirmation);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("status").getValue().toString() == "1") {

                        startActivity(new Intent(ConfirmationActivity.this, FarmerMainPanel.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

                    Intent intent = new Intent(ConfirmationActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            }
        };

        //check
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConfirmation();
            }
        });


    }

    private void checkConfirmation() {

        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Checking status...");
        p.setCanceledOnTouchOutside(false);
        p.show();

        mUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("status").getValue().toString() == "1") {

                    p.dismiss();
                    Toast.makeText(ConfirmationActivity.this, "Registration approved.Redirecting...", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(ConfirmationActivity.this, FarmerMainPanel.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }, 2000);

                } else {

                    p.dismiss();
                    Toast toasy = Toast.makeText(ConfirmationActivity.this, "Registration not yet approved.Try later!", Toast.LENGTH_LONG);
                    toasy.setGravity(Gravity.CENTER, 0, 0);
                    toasy.show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null) {
            mAuth.removeAuthStateListener(mListener);
        }
    }
}
