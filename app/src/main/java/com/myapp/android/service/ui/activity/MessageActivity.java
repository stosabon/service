package com.myapp.android.service.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.myapp.android.service.ui.fragment.MessageFragment;
import com.myapp.android.service.R;
import com.myapp.android.service.util.UserExtraConstants;

/**
 * Created by pro on 31.10.2017.
 */

public class MessageActivity extends AppCompatActivity implements MessageFragment.ReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(toolbar);

       // if (getSupportActionBar() != null) {
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // }

       // setTitle(getIntent().getStringExtra(UserExtraConstants.EXTRA_USER_NAME));
    }

    @Override
    public String getReceiverUid() {
        return getIntent().getStringExtra(UserExtraConstants.EXTRA_USER_UID);
    }

    @Override
    public String getReceiverName() {
        return getIntent().getStringExtra(UserExtraConstants.EXTRA_USER_NAME);
    }

    @Override
    public String getReceiverPhotoUrl() {
        return getIntent().getStringExtra(UserExtraConstants.EXTRA_USER_PHOTO_URL);
    }
}
