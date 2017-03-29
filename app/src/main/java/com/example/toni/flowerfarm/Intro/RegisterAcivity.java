package com.example.toni.flowerfarm.Intro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toni.flowerfarm.Farmer.ConfirmationActivity;
import com.example.toni.flowerfarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAcivity extends AppCompatActivity {

    private EditText name, email, id_number, mobile, farmName, password;
    private Button register;

    private FirebaseAuth mAuth;
    private DatabaseReference mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acivity);

        //views
        name = (EditText) findViewById(R.id.et_register_name);
        email = (EditText) findViewById(R.id.et_register_email);
        id_number = (EditText) findViewById(R.id.et_register_id);
        farmName = (EditText) findViewById(R.id.et_register_farm);
        password = (EditText) findViewById(R.id.et_register_pass);
        mobile = (EditText) findViewById(R.id.et_register_mobile);

        register = (Button) findViewById(R.id.btn_register);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void addUser() {

        if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(id_number.getText()) && !TextUtils.isEmpty(mobile.getText())
                && !TextUtils.isEmpty(farmName.getText()) && !TextUtils.isEmpty(password.getText())) {

            final ProgressDialog progressDialog = new ProgressDialog(RegisterAcivity.this);
            progressDialog.setMessage("Adding farmer...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        //account created successfully
                        //get user id
                        String userId = mAuth.getCurrentUser().getUid();

                        DatabaseReference current = mUsers.child(userId);
                        current.child("name").setValue(name.getText().toString());
                        current.child("id_number").setValue(id_number.getText().toString());
                        current.child("mobile_number").setValue(mobile.getText().toString());
                        current.child("status").setValue(0);
                        current.child("category").setValue("farmer");
                        current.child("farm_name").setValue(farmName.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.dismiss();

                                if (task.isSuccessful()){
                                    //redirect to confirmation
                                    startActivity(new Intent(RegisterAcivity.this,ConfirmationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                                }else {
                                    Toast.makeText(RegisterAcivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterAcivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(RegisterAcivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            Toast.makeText(RegisterAcivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }
}
