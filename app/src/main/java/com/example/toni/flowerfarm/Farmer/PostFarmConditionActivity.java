package com.example.toni.flowerfarm.Farmer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.toni.flowerfarm.Intro.LoginActivity;
import com.example.toni.flowerfarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostFarmConditionActivity extends AppCompatActivity {

    private Button submit;
    private EditText name, comments;
    private ImageButton image;

    // Activity request codes
    private static final int GAllERY_REQUEST = 100;


    private Uri fileUri = null;

    private Bitmap bitmap;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_farm_condition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Post Farm condition");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //views
        submit = (Button) findViewById(R.id.btn_postFarm_submit);
        name = (EditText) findViewById(R.id.et_postFarm_name);
        comments = (EditText) findViewById(R.id.et_postFarm_desc);
        image = (ImageButton) findViewById(R.id.imgnbtn_postFarm_image);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(PostFarmConditionActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        };



        //listener
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureimage();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void submitData() {

        if (fileUri != null) {

            if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(comments.getText())) {

                final ProgressDialog progressDialog = new ProgressDialog(PostFarmConditionActivity.this);
                progressDialog.setMessage("Submitting post...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                //create posts db

                final DatabaseReference mPosts = FirebaseDatabase.getInstance().getReference().child("Posts");
               final DatabaseReference curr =  mPosts.push();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Farm_pics").child(curr.getKey());
                storageReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        curr.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                curr.child("name").setValue(name.getText().toString());
                                curr.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                curr.child("date").setValue(today());
                                curr.child("comment").setValue(comments.getText().toString());
                                curr.child("image").setValue(taskSnapshot.getDownloadUrl().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();

                                            AlertDialog alertDialog = new AlertDialog.Builder(PostFarmConditionActivity.this).create();
                                            alertDialog.setTitle("Success");
                                            alertDialog.setMessage("Your farm proceedings have been received. Wait for the expert officer's remarks");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            finish();
                                                        }
                                                    });
                                            alertDialog.show();

                                        } else {

                                            progressDialog.dismiss();
                                            Toast.makeText(PostFarmConditionActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                progressDialog.dismiss();
                                Toast.makeText(PostFarmConditionActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(PostFarmConditionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            } else {
                Toast.makeText(PostFarmConditionActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(PostFarmConditionActivity.this, "Take a pictire of the farm", Toast.LENGTH_SHORT).show();
        }
    }

    private String today() {
        Date date = new Date();
        //Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringdate = dt.format(date);

        System.out.println("Submission Date: " + stringdate);
        return stringdate;
    }


    private void captureimage() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GAllERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == GAllERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                CropImage.activity(data.getData())
                        .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);


            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                fileUri = result.getUri();

                image.setImageURI(fileUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

                Log.d("Cropping",error.getMessage());
            }
        }
    }


    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}
