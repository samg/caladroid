package com.drasticcode.caladroid;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drasticcode.caladroid.Event.Venue;
import com.google.android.maps.GeoPoint;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EventActivity extends Activity {
	LinearLayout linearLayout;
	private GeoPoint geoPoint;
	private double lat;
	private double lng;
	private Event event;
	private Venue venue;
	public void callIntent(View view) {
		Intent intent = null;
		DecimalFormat df = new DecimalFormat("#.#######");
		switch (view.getId()) {
		case R.id.Button01:
			String uri = "geo:"+df.format(lat)+","+df.format(lng)+"?q="+venue.address()+"&z=19";
			//System.out.println(uri);
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(uri));
			startActivity(intent);
			break;
		}
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		int position = i.getIntExtra("event_index", -1);
		JsonArray events = UpcomingActivity.events;
		JsonObject json = (JsonObject) events.get(position);
		event = new Event(json);
		venue = event.getVenue();

		this.setContentView(R.layout.event);

		lat= venue.getLatitude();
		lng = venue.getLongitude();

		// Toast.makeText(this, e.when(), Toast.LENGTH_LONG)
		// .show();

		TextView event_title = (TextView) findViewById(R.id.event_title);
		event_title.setText(event.title());

		TextView event_when = (TextView) findViewById(R.id.event_when);
		event_when.setText(event.when());

		TextView venue_title = (TextView) findViewById(R.id.venue_title);
		venue_title.setText(event.getVenue().title());

		TextView venue_address = (TextView) findViewById(R.id.venue_address);
		venue_address.setText(event.getVenue().address());

		TextView venue_wifi = (TextView) findViewById(R.id.venue_wifi);
		if (event.getVenue().isWifi()) {
			venue_wifi.setText("Public Wifi");
		}

		// MapView venue_map = (MapView) findViewById(R.id.venue_map);

		TextView event_website = (TextView) findViewById(R.id.event_website);
		event_website.setText(event.website());

		TextView event_description = (TextView) findViewById(R.id.event_description);
		event_description.setText(event.description());
	}

}
