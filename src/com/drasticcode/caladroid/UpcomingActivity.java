package com.drasticcode.caladroid;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.drasticcode.caladroid.Event.Venue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class UpcomingActivity extends ListActivity {
	static public JsonArray events;
	

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		
		// Create an array of Strings, that will be put to our ListActivity
		ArrayList<String> names = new ArrayList<String>();

		for(JsonElement e : events){
			Event event = new Event((JsonObject) e);

			Venue venue = event.venue;
			String text = event.title().concat("\n").concat(event.when()).concat("\n").concat(venue.title());
			names.add(text);
		}
		
		
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		setContentView(R.layout.upcoming);
		this.setListAdapter(new ArrayAdapter<String>(this,
				R.layout.list_item, names));
	}
	
	protected void onListItemClick (ListView l, View v, int position, long id){
		JsonElement event = UpcomingActivity.events.get(position);
		
		Intent i = new Intent(UpcomingActivity.this,EventActivity.class);
		i.putExtra("event_index", position);
		startActivity(i);
	}
}
