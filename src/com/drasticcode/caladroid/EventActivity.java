package com.drasticcode.caladroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.MapView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EventActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i = getIntent();
        int position = i.getIntExtra("event_index", -1);
        JsonArray events = UpcomingActivity.events ;
        JsonObject json = (JsonObject) events.get(position);
        Event event = new Event(json);
//		Toast.makeText(this, e.when(), Toast.LENGTH_LONG)
//		.show();
        

        this.setContentView(R.layout.event);
        TextView event_title = (TextView) findViewById(R.id.event_title);
        event_title.setText(event.title());

        TextView event_when = (TextView) findViewById(R.id.event_when);
        event_when.setText(event.when());
        
        TextView venue_title = (TextView) findViewById(R.id.venue_title);
        venue_title.setText(event.venue.title());

        //MapView venue_map = (MapView) findViewById(R.id.venue_map);
        
        TextView event_website = (TextView) findViewById(R.id.event_website);
        event_website.setText(event.website());
        
        TextView event_description = (TextView) findViewById(R.id.event_description);
        event_description.setText(event.description());
	}
}
