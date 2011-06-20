package com.drasticcode.caladroid;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.JsonObject;

public class Event {
	private String title, website, description;
	public Date start, end;
	public Venue venue;
	public Event(JsonObject json) {

		title = json.get("title").getAsString();
		start = parseDate(json.get("start_time").getAsString());
		end = parseDate(json.get("end_time").getAsString());
		if ( json.has(website) ) {
			website = json.get("website").getAsString();
		}
		description = json.get("description").getAsString();

		// venue = json.getAsJsonObject("venue").get("title").getAsString();
	}

	public String title() {
		return title;
	}
	public String website() {
		if ( website != null ){
			return website;
		} else {
			return "";
		}
	}
	public String description() {
		return description;
	}

	private Date parseDate(String string) {
		// 2011-06-20T18:30:00-07:00
		SimpleDateFormat reader = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date date = new Date();
		try {
			date = reader.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public String formattedStartDate() {
		// Monday, June 20, 2011 from 9am�7pm
		SimpleDateFormat writer = new SimpleDateFormat(
				"EEEEEEEEEEE, MMMMMMMMMMMMMM d, yyyy");
		String str = writer.format(start);
		return str;
	}

	public String formattedStartTime() {
		// Monday, June 20, 2011 from 9am�7pm
		SimpleDateFormat writer = new SimpleDateFormat("ha");
		String str = writer.format(start);
		return str;
	}

	public String formattedEndTime() {
		// Monday, June 20, 2011 from 9am�7pm
		SimpleDateFormat writer = new SimpleDateFormat("ha");
		String str = writer.format(end);
		return str;
	}

	public String when() {
		return formattedStartDate() + " from " + formattedStartTime() + "-"
				+ formattedEndTime();
	}

	public class Venue {
		public Venue(JsonObject json){
			json.getAsJsonObject("venue");
		}
	}
}