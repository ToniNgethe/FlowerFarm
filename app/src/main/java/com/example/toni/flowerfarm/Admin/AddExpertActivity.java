package com.example.toni.flowerfarm.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toni.flowerfarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExpertActivity extends AppCompatActivity {

    private EditText name, email, password;
    private Button add;
    private FirebaseAuth mAuth;
    private DatabaseReference mExperts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Farm expert");

        //views
        name = (EditText) findViewById(R.id.et_addexpert_name);
        email = (EditText) findViewById(R.id.et_addexpert_email);
        password = (EditText) findViewById(R.id.et_addexpert_pass);
        add = (Button) findViewById(R.id.btn_addexpert_submit);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mExperts = FirebaseDatabase.getInstance().getReference().child("Users");

        //listener
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEXpert();
            }
        });
    }

    private void addEXpert() {

        if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(password.getText())) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Adding expert...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        DatabaseReference current = mExperts.child(mAuth.getCurrentUser().getUid());

                        current.child("name").setValue(name.getText().toString());
                        current.child("email").setValue(email.getText().toString());
                        current.child("password").setValue(password.getText().toString());
                        current.child("category").setValue("expert").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.dismiss();

                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();

                                    AlertDialog alertDialog = new AlertDialog.Builder(AddExpertActivity.this).create();
                                    alertDialog.setTitle("Success");
                                    alertDialog.setMessage("Expert Added!");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                } else {
                                    Toast.makeText(getApplication(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(getApplication(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            Toast.makeText(AddExpertActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }

}
