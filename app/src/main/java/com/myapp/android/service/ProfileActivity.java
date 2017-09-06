package com.myapp.android.service;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(), "People");
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new ProfileFragment(), "Calls");
        viewPager.setAdapter(adapter);
    }
}
      /*  mUserNameTextView = (TextView) findViewById(R.id.user_name);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Log.d(TAG, "" + mAuth.getCurrentUser());
                String currentUid = mAuth.getCurrentUser().getUid();
                mDatabase.child("users").child(currentUid).addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        mUserNameTextView.setText(dataSnapshot.child("name").getValue().toString());
        }

@Override
public void onCancelled(DatabaseError databaseError) {

        }
        }); */