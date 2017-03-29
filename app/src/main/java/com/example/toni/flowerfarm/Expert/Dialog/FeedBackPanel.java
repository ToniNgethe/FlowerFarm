package com.example.toni.flowerfarm.Expert.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toni.flowerfarm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by toni on 3/26/17.
 */

public class FeedBackPanel extends Dialog {

    private Context ctx;
    private TextView indicator;
    private EditText feedback;
    private ProgressBar mProgressBar;
    private Button submit;
    private ImageButton cancel;
    private String post;

    private DatabaseReference mPosts;

    public FeedBackPanel(@NonNull Context context, String post) {
        super(context);

        this.ctx = context;
        this.post = post;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);

        //views
        cancel = (ImageButton) findViewById(R.id.ib_dialogfeedbac_cancel);
        indicator = (TextView) findViewById(R.id.tv_dialogfeedbac_indicator);
        feedback = (EditText) findViewById(R.id.et_dialogfeedbac_fb);
        mProgressBar = (ProgressBar) findViewById(R.id.progreess_dialogfeedbac);
        submit = (Button) findViewById(R.id.btn_dialogfeedbac_submit);

        //Firebase
        mPosts = FirebaseDatabase.getInstance().getReference().child("Posts");


        //listeners
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final boolean[] done = {true};

                mProgressBar.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(feedback.getText())) {

                    mPosts.child(post).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (done[0]) {

                                dataSnapshot.getRef().child("feedback").setValue(feedback.getText().toString());
                                mProgressBar.setVisibility(View.GONE);
                                indicator.setText("Feedback submitted successfully");

                                dismiss();
                                done[0] = false;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(ctx,"Field cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
