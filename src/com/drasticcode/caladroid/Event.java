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
	private Venue venue;
	public Event(JsonObject json) {

		title = json.get("title").getAsString();
		start = parseDate(json.get("start_time").getAsString());
		end = parseDate(json.get("end_time").getAsString());
		if ( json.has(website) ) {
			website = json.get("website").getAsString();
		}
		description = json.get("description").getAsString();
		JsonObject v = json.getAsJsonObject("venue");
		setVenue(new Venue(v));
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
	
	public String formattedEndDate() {
		// Monday, June 20, 2011 from 9am�7pm
		SimpleDateFormat writer = new SimpleDateFormat(
				"EEEEEEEEEEE, MMMMMMMMMMMMMM d, yyyy");
		String str = writer.format(end);
		return str;
	}

	public String formattedStartTime() {
		// Monday, June 20, 2011 from 9am�7pm
		SimpleDateFormat writer = new SimpleDateFormat("ha");
		String str = writer.format(start);
		return str.toLowerCase();
	}

	public String formattedEndTime() {
		// Monday, June 20, 2011 from 9am�7pm
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
			if (json.has(string)&& !json.get(string).isJsonNull()) {
				return json.get(string).getAsString();
			}
			return "";
		}
		
		private Boolean getBooleanAttribute(String string) {
			if (json.has(string)&& !json.get(string).isJsonNull()) {
				return json.get(string).getAsBoolean();
			}
			return false;
		}
		
		private Double getDoubleAttribute(String string) {
			if (json.has(string)&& !json.get(string).isJsonNull()) {
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
}
