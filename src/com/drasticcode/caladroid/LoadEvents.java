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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class LoadEvents extends AsyncTask<String, String, JsonArray> {
	public String responseString;
	public Context mContext;


	protected void onPostExecute(JsonArray result) {

		Intent i = new Intent(mContext,UpcomingActivity.class);
		mContext.startActivity(i);

	}

	@Override
	protected JsonArray doInBackground(String... params) {

		String uri = "http://calagator.org/events.json";
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpGet(uri));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else{
				//Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			//TODO Handle problems..
		} catch (IOException e) {
			//TODO Handle problems..
		}
		Gson gson = new Gson ();
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(responseString).getAsJsonArray();
		UpcomingActivity.events = array;

		return array;
	}
}


