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
	private int id;
	private Venue venue;
	
	private String getStringAttribute(JsonObject json, String string) {
		if (json != null && json.has(string)&& !json.get(string).isJsonNull()) {
			return json.get(string).getAsString();
		}
		return "";
	}
	public Event(JsonObject json) {

		title = json.get("title").getAsString();
		start = parseDate(getStringAttribute(json, "start_time"));
		end = parseDate(getStringAttribute(json, "end_time"));
		id = json.get("id").getAsInt();
		website = getStringAttribute(json, "website");
		description = getStringAttribute(json, "description");
		JsonObject v = json.getAsJsonObject("venue");
		setVenue(new Venue(v));
	}

	public String title() {
		return title;
	}
	public String website() {
		return website;
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
		// Monday, June 20, 2011 from 9am–7pm
		SimpleDateFormat writer = new SimpleDateFormat(
				"EEEEEEEEEEE, MMMMMMMMMMMMMM d, yyyy");
		String str = writer.format(start);
		return str;
	}
	
	public String formattedEndDate() {
		// Monday, June 20, 2011 from 9am–7pm
		SimpleDateFormat writer = new SimpleDateFormat(
				"EEEEEEEEEEE, MMMMMMMMMMMMMM d, yyyy");
		String str = writer.format(end);
		return str;
	}

	public String formattedStartTime() {
		SimpleDateFormat writer = new SimpleDateFormat("ha");
		String str = writer.format(start);
		return str.toLowerCase();
	}

	public String formattedEndTime() {
		SimpleDateFormat writer = new SimpleDateFormat("ha");
		String str = writer.format(end);
		return str.toLowerCase();
	}

	public String when() {
		return formattedStartDate() + " from " + formattedDuration();
	}
	
	public String formattedDuration(){
		if (!formattedStartDate().equals(formattedEndDate())){
			return formattedStartTime() + " through " + formattedEndDate() + " at " + formattedEndTime();
		}
		return formattedStartTime() + "-" + formattedEndTime();
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public Venue getVenue() {
		return venue;
	}

	public class Venue {
		private String title, address, description;
		private double latitude, longitude;
		private boolean wifi;
		private String street_address;
		private String locality;
		private String postal_code;
		private String country;
		private String region;
		private JsonObject json;
		
		
		public Venue(JsonObject json){
			this.json = json;
			address = getStringAttribute("address");
			title = getStringAttribute("title");
			street_address = getStringAttribute("street_address");
			locality = getStringAttribute("locality");
			country = getStringAttribute("country");
			postal_code = getStringAttribute("postal_code");
			region = getStringAttribute("region");
			description = getStringAttribute("description");
			wifi = getBooleanAttribute("wifi");
			latitude = getDoubleAttribute("latitude");
			longitude = getDoubleAttribute("longitude");
			
		}


		private String getStringAttribute(String string) {
			if (json != null && json.has(string)&& !json.get(string).isJsonNull()) {
				return json.get(string).getAsString();
			}
			return "";
		}
		
		private Boolean getBooleanAttribute(String string) {
			if (json != null && json.has(string)&& !json.get(string).isJsonNull()) {
				return json.get(string).getAsBoolean();
			}
			return false;
		}
		
		private Double getDoubleAttribute(String string) {
			if (json != null && json.has(string)&& !json.get(string).isJsonNull()) {
				return json.get(string).getAsDouble();
			}
			return -1.0;
		}


		public String title() {
			return title;
		}


		public double getLatitude() {
			return latitude;
		}



		public double getLongitude() {
			return longitude;
		}


		public void setWifi(boolean wifi) {
			this.wifi = wifi;
		}


		public boolean isWifi() {
			return wifi;
		}


		public void setAddress(String address) {
			this.address = address;
		}


		public String getAddress() {
			return address;
		}
		
		public String address(){
			return street_address + ", " + locality + ", " + region + " " + postal_code + ", " + country;
		}
		
	}

	public int getId() {
		return id;
	}

	public String shareString() {
		return title() + "\n" + "http://calagator.org/events/" + Integer.toString(getId()) + "\n" + when();
	}

}
