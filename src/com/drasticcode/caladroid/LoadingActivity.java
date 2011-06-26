package com.drasticcode.caladroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoadingActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.main);
    	LoadEvents tr = new LoadEvents();
    	tr.mContext = LoadingActivity.this;
        tr.execute();
    }
}