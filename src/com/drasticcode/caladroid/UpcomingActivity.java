package com.drasticcode.caladroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private	SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEEEEEEEE, MMMMMMMMMMMMMM d, yyyy"); 

	public Map<String,?> createItem(String title, String caption) {
		Map<String,String> item = new HashMap<String,String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		return item;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.upcoming_menu, menu);
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.upcoming_refresh:
	    	return loadEvents(true);
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	private boolean loadEvents(boolean b) {
		LoadEvents tr = new LoadEvents();
		tr.mContext = UpcomingActivity.this;
		tr.execute(b);
		this.setContentView(R.layout.main);
		return true;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		events_indexes = new HashMap<Integer,Integer>();
		
		Date lastDate = new Date(1970, 1, 1);
		Date eventStart = null;
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		List<Map<String,?>> dayList = new LinkedList<Map<String,?>>();
		int counter = 0;
		int offset = 1;
		events_indexes.put(offset, counter);
		if(events == null) {
			loadEvents(false);
			return;
		}
		for(JsonElement e : events){
			Event event = new Event((JsonObject) e);
			Venue venue = event.getVenue();
			eventStart = event.start;
			
			if (counter > 0){
				if (! dateFormat.format(eventStart).equals( dateFormat.format(lastDate) ) ) {
					adapter.addSection(sectionLabel(lastDate), new SimpleAdapter(this, dayList, R.layout.list_complex,
							new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
					dayList = new LinkedList<Map<String,?>>();
					offset++;
				}
			}
			dayList.add(createItem(event.title(), venue.title().concat("\n").concat(event.formattedDuration())));
			events_indexes.put(offset, counter);
			lastDate = eventStart;
			counter++;
			offset++;
			
		}

		ListView list = new ListView(this);
		EventClickListener listener = new EventClickListener();
		list.setOnItemClickListener(listener);
		list.setAdapter(adapter);
		this.setContentView(list);
	}

	private String sectionLabel(Date lastDate) {
		String formattedDate = dateFormat.format(lastDate);
		Calendar today = Calendar.getInstance();
		String formattedToday = dateFormat.format(Calendar.getInstance().getTime());
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DATE, 1);
		String formattedTomorrow = dateFormat.format(tomorrow.getTime());

		if (formattedDate.equals(formattedToday)) {
			return "Today, " + formattedDate;
		} else if ( formattedDate.equals(formattedTomorrow)) {
			return "Tomorrow, " + formattedDate;
		} else if (lastDate.before(today.getTime())){
			return "Started " + formattedDate;
		} else {
			return formattedDate;
		}

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
