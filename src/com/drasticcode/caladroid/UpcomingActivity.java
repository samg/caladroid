package com.drasticcode.caladroid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class UpcomingActivity extends ListActivity {
	static public String responseString;
	

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		
		Gson gson = new Gson ();
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(responseString).getAsJsonArray();

		// Create an array of Strings, that will be put to our ListActivity
		ArrayList<String> names = new ArrayList<String>();

		for(JsonElement e : array){
			String title = ((JsonObject) e).get("title").getAsString();
			String start_time = ((JsonObject) e).get("start_time").getAsString();
			String venue = ((JsonObject) e).getAsJsonObject("venue").get("title").getAsString();

			String text = title.concat("\n").concat(start_time).concat("\n").concat(venue);
			names.add(text);
		}
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		this.setListAdapter(new ArrayAdapter<String>(this,
				R.layout.list_item, names));
	}
}
