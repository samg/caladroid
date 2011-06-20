package com.drasticcode.caladroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CaladroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.main);
    	//Intent upcoming = new Intent(CaladroidActivity.this, UpcomingActivity.class);
    	//this.startActivity(upcoming);
    	LoadEvents tr = new LoadEvents();
    	tr.mContext = CaladroidActivity.this;
        tr.execute();
    }
}