package com.drasticcode.caladroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drasticcode.caladroid.Event.Venue;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EventActivity extends MapActivity {
	LinearLayout linearLayout;
	private GeoPoint geoPoint;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		int position = i.getIntExtra("event_index", -1);
		JsonArray events = UpcomingActivity.events ;
		JsonObject json = (JsonObject) events.get(position);
		Event event = new Event(json);
		Venue venue = event.getVenue();

		this.setContentView(R.layout.event);
		MapView mapView = (MapView) findViewById(R.id.venue_map);
		mapView.setBuiltInZoomControls(true);
		MapController mapController = mapView.getController();

		double latitude = venue.getLatitude() * 1E6;
		double longitude = venue.getLongitude() * 1E6;
		System.out.println(venue.getLatitude());
		System.out.println(latitude);
		int lat = (int) latitude;
		int lng = (int) longitude;
		System.out.println(lat);

		System.out.println(latitude);
		geoPoint = new GeoPoint(lat, lng);
		mapController.setCenter(geoPoint);
		mapController.setZoom(18);
		Drawable marker=getResources().getDrawable(android.R.drawable.arrow_down_float);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		mapView.getOverlays().add(new InterestingLocations(marker, geoPoint));
		//		Toast.makeText(this, e.when(), Toast.LENGTH_LONG)
		//		.show();


		TextView event_title = (TextView) findViewById(R.id.event_title);
		event_title.setText(event.title());

		TextView event_when = (TextView) findViewById(R.id.event_when);
		event_when.setText(event.when());

		TextView venue_title = (TextView) findViewById(R.id.venue_title);
		venue_title.setText(event.getVenue().title());

		//MapView venue_map = (MapView) findViewById(R.id.venue_map);

		TextView event_website = (TextView) findViewById(R.id.event_website);
		event_website.setText(event.website());

		TextView event_description = (TextView) findViewById(R.id.event_description);
		event_description.setText(event.description());
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	class InterestingLocations extends ItemizedOverlay<OverlayItem>{

		private List<OverlayItem> locations = new ArrayList<OverlayItem>();
		private Drawable marker;

		public InterestingLocations(Drawable defaultMarker, GeoPoint geoPoint){
			super(defaultMarker);
			// TODO Auto-generated constructor stub
			// create locations of interest
			locations.add(new OverlayItem(geoPoint, "My Place", "My Place"));
			populate();
		}


		@Override
		protected OverlayItem createItem(int i) {
			return locations.get(i);
		}

		@Override
		public int size() {
			return locations.size();
		}
		@Override
		public void draw(Canvas canvas, MapView mapView, 
				boolean shadow) {
			super.draw(canvas, mapView, shadow);

			boundCenterBottom(marker);
		}
	}
}
