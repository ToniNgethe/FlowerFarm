package com.example.toni.flowerfarm.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.toni.flowerfarm.Admin.Fragments.Experts;
import com.example.toni.flowerfarm.Admin.Fragments.FarmPosts;
import com.example.toni.flowerfarm.Admin.Fragments.Farmers;
import com.example.toni.flowerfarm.Admin.Fragments.Reports;
import com.example.toni.flowerfarm.Admin.ViewPagerAdapter.AdminViewPagerAdapters;
import com.example.toni.flowerfarm.Intro.LoginActivity;
import com.example.toni.flowerfarm.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminPanel extends AppCompatActivity {

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        //views
        toolbar = (Toolbar) findViewById(R.id.toolbar_adminpanel);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_adminpanel);
        viewPager = (ViewPager) findViewById(R.id.viewpager_adminpanel);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //setup viewpager
        setUpViewPager();

        //setupTab with viewPager
        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(AdminPanel.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

            }
        };

    }

    private void setUpViewPager() {

        Handler mHandler = new Handler();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                AdminViewPagerAdapters ada = new AdminViewPagerAdapters(getSupportFragmentManager());
                ada.addFragment(new Experts(),"Farm Experts");
                ada.addFragment(new FarmPosts(),"Farm Posts");
                ada.addFragment(new Farmers(),"Farmers");
                ada.addFragment(new Reports(),"Report");

                viewPager.setAdapter(ada);

            }
        };

        if (runnable != null)
            mHandler.post(runnable);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add:

                startActivity(new Intent(AdminPanel.this, AddExpertActivity.class));

                break;
            case R.id.action_logout:

                mAuth.signOut();
                startActivity( new Intent(AdminPanel.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
