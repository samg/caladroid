package com.drasticcode.caladroid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.drasticcode.caladroid.Event.Venue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class UpcomingActivity extends Activity {
	private static final String ITEM_TITLE = "title";
	private static final String ITEM_CAPTION = "caption";
	static public JsonArray events;
	static public HashMap<Integer, Integer> events_indexes;

	public Map<String,?> createItem(String title, String caption) {
		Map<String,String> item = new HashMap<String,String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		return item;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		events_indexes = new HashMap<Integer,Integer>();
		


		Date last_date = new Date(1970, 1, 1);
		Date date = null;
		SimpleDateFormat day = new SimpleDateFormat("EEEEEEEEEEE, MMMMMMMMMMMMMM d, yyyy"); 
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		List<Map<String,?>> today = new LinkedList<Map<String,?>>();
		int counter = 0;
		int offset = 1;
		events_indexes.put(offset, counter);
		for(JsonElement e : events){
			Event event = new Event((JsonObject) e);
			Venue venue = event.getVenue();
			date = event.start;
			
			if (counter > 0){
				if (! day.format(date).equals( day.format(last_date) ) ) {
					
					adapter.addSection(day.format(last_date), new SimpleAdapter(this, today, R.layout.list_complex,
							new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
					today = new LinkedList<Map<String,?>>();
					offset++;
				}
			}
			today.add(createItem(event.title(), venue.title().concat("\n").concat(event.formattedDuration())));
			events_indexes.put(offset, counter);
			last_date = date;
			counter++;
			offset++;
			
		}

		ListView list = new ListView(this);
		EventClickListener listener = new EventClickListener();
		list.setOnItemClickListener(listener);
		list.setAdapter(adapter);
		this.setContentView(list);

//		setContentView(R.layout.upcoming);
//		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, names));
	}

	class EventClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			System.out.println(arg2);
			Intent i = new Intent(UpcomingActivity.this, EventActivity.class);
			int event_index = (Integer) UpcomingActivity.events_indexes.get(arg2);
			i.putExtra("event_index", event_index);
			startActivity(i);
			
		}
	}
}
