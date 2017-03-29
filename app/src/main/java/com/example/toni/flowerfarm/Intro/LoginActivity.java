package com.example.toni.flowerfarm.Intro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toni.flowerfarm.Admin.AdminPanel;
import com.example.toni.flowerfarm.Expert.ExpertPanelActivity;
import com.example.toni.flowerfarm.Farmer.ConfirmationActivity;
import com.example.toni.flowerfarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText email, password;
    private FirebaseAuth mAuth;
    private Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //views
        email = (EditText) findViewById(R.id.et_signin_email);
        password = (EditText) findViewById(R.id.et_signin_pass);
        login = (Button) findViewById(R.id.btn_login_login);
        signup = (Button) findViewById(R.id.btn_login_signup);

        //listeners
        login.setOnClickListener(this);
        signup.setOnClickListener(this);

        ///firebase
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_login:

                loginUser();

                break;
            case R.id.btn_login_signup:

                openRegister();

                break;
        }
    }

    private void loginUser() {

        if (!TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(password.getText())) {

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Authenticating user..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        //redirect to mainpage
                        //check which type of user

                        final DatabaseReference check = FirebaseDatabase.getInstance().getReference().child("Users");

                        check.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                progressDialog.dismiss();

                                if (dataSnapshot.exists()) {

                                    Log.d(TAG, mAuth.getCurrentUser().getUid() + " data " + dataSnapshot);

                                    if (dataSnapshot.child("category").getValue().toString().equals("farmer")) {

                                        startActivity(new Intent(LoginActivity.this, ConfirmationActivity.class));
                                        Toast.makeText(LoginActivity.this, "Logged in as Farmer", Toast.LENGTH_SHORT).show();

                                    } else if (dataSnapshot.child("category").getValue().toString().equals("admin")) {

                                        startActivity(new Intent(LoginActivity.this, AdminPanel.class));
                                        Toast.makeText(LoginActivity.this, "Logged in as Admin", Toast.LENGTH_SHORT).show();

                                    } else if (dataSnapshot.child("category").getValue().toString().equals("expert")) {

                                        startActivity(new Intent(LoginActivity.this, ExpertPanelActivity.class));
                                        Toast.makeText(LoginActivity.this, "Logged in as Farm expert", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Toast.makeText(LoginActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }else {

                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            Toast.makeText(LoginActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void openRegister() {

        startActivity(new Intent(LoginActivity.this,RegisterAcivity.class));
    }


}
