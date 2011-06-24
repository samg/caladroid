package com.drasticcode.caladroid;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.webkit.WebView;
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

		MapView mapView = (MapView) findViewById(R.id.venue_map);
		mapView.setBuiltInZoomControls(true);
		MapController mapController = mapView.getController();

		double latitude = venue.getLatitude() * 1E6;
		double longitude = venue.getLongitude() * 1E6;

		int lat = (int) latitude;
		int lng = (int) longitude;

		geoPoint = new GeoPoint(lat, lng);
		mapController.setCenter(geoPoint);
		mapController.setZoom(16);
		Drawable marker = getResources().getDrawable(
				R.drawable.marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		mapView.getOverlays().add(new InterestingLocations(marker, geoPoint));

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
		
		String text = event.description().replaceAll("\n", "<br/>");
		Spanned fromHtml = Html.fromHtml(text);
		event_description.setText(fromHtml);
		Linkify.addLinks(event_description, Linkify.ALL);


	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class InterestingLocations extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> locations = new ArrayList<OverlayItem>();
		private Drawable marker;

		public InterestingLocations(Drawable defaultMarker, GeoPoint geoPoint) {
			super(defaultMarker);
			marker = defaultMarker;
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
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

			boundCenterBottom(marker);
		}
	}
}
